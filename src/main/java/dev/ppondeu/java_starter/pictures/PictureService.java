package dev.ppondeu.java_starter.pictures;

import dev.ppondeu.java_starter.common.exceptions.BadRequestException;
import dev.ppondeu.java_starter.common.exceptions.NotFoundException;
import dev.ppondeu.java_starter.common.interfaces.IFileStorageService;
import dev.ppondeu.java_starter.pictures.dtos.PictureCreateDTO;
import dev.ppondeu.java_starter.pictures.dtos.PictureUpdateDTO;
import dev.ppondeu.java_starter.pictures.entities.Picture;
import dev.ppondeu.java_starter.pictures.entities.PicturePermission;
import dev.ppondeu.java_starter.pictures.entities.PicturePermissionType;
import dev.ppondeu.java_starter.pictures.entities.PictureUpdateType;
import dev.ppondeu.java_starter.pictures.interfaces.IPicturePermissionRepository;
import dev.ppondeu.java_starter.pictures.interfaces.IPictureRepository;
import dev.ppondeu.java_starter.pictures.interfaces.IPictureService;
import dev.ppondeu.java_starter.pictures.mappers.PictureMapper;
import dev.ppondeu.java_starter.users.entities.User;
import dev.ppondeu.java_starter.users.interfaces.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PictureService implements IPictureService {

    private final IPictureRepository pictureRepository;
    private final IPicturePermissionRepository picturePermissionRepository;
    private final IUserService userService;
    private final IFileStorageService fileStorageService;

    public PictureService(
            IPictureRepository pictureRepository,
            IPicturePermissionRepository picturePermissionRepository,
            IUserService userService,
            IFileStorageService fileStorageService
    ) {
        this.pictureRepository = pictureRepository;
        this.picturePermissionRepository = picturePermissionRepository;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Picture createPicture(UUID userId, MultipartFile file) {
        var user = this.userService.getUserById(userId);
        var pictureCreateDTO = new PictureCreateDTO(file, userId);

        String filePath = this.fileStorageService.store(file);
        pictureCreateDTO.setFilePath(filePath);
        System.out.println(pictureCreateDTO.getFilePath());
        var picture = PictureMapper.mapToPicture(pictureCreateDTO, user);
        var newPicture = this.pictureRepository.save(picture);
        var picturePermission = PictureMapper.mapToPicturePermission(user, picture, PicturePermissionType.ALL);
        this.picturePermissionRepository.save(picturePermission);
        return newPicture;
    }

    @Override
    public List<Picture> getUserPictures(UUID userId) {
        return pictureRepository.findPicturesByUser(userId);
    }

    @Override
    public Picture getPicture(UUID pictureId, UUID currentUserId) {
//        check permissions
        var picture = pictureRepository.findById(pictureId).orElseThrow(() -> new NotFoundException("Picture with id: "  + pictureId + " not found"));
        boolean hasPermission = false;
        for (PicturePermission picturePermission : picture.getPermissions()) {
            System.out.println(picturePermission + " | " + currentUserId);
            if (picturePermission.getUser().getId().equals(currentUserId)) {
                hasPermission = true;
                break;
            }
        }

        if (!hasPermission) {
            throw new BadRequestException("You don't have permission to access this picture");
        }

        return picture;
    }

    @Override
    @Transactional
    public void deletePicture(UUID pictureId, UUID currentUserId) {
        var picture = this.pictureRepository.findById(pictureId).orElseThrow(() -> new NotFoundException("Picture with id: "  + pictureId + " not found"));
        System.out.println("picture " + picture);
        if (!picture.getUser().getId().equals(currentUserId)) {
            throw new BadRequestException("You don't have permission to delete this picture");
        }
        pictureRepository.deleteById(pictureId);
    }

    @Override
    public void softDeletePicture(UUID pictureId, UUID currentUserId) {

    }

    @Override
    public Picture updatePictureDetails(UUID pictureId, PictureUpdateType updateType, UUID editorId, PictureUpdateDTO pictureUpdateDTO) {
        var picture = this.pictureRepository.findById(pictureId)
                .orElseThrow(() -> new NotFoundException("Picture with id: "  + pictureId + " not found"));

        if (updateType == PictureUpdateType.DELETE && this.canUserDelete(editorId, picture)) {
            if (picture.getdeletedAt() == null) {
                picture.setdeletedAt(LocalDateTime.now());
            } else {
                throw new BadRequestException("You can't delete this picture");
            }
        } else if (updateType == PictureUpdateType.RESTORE && this.canUserDelete(editorId, picture)) {
            if (picture.getdeletedAt() == null) {
                throw new BadRequestException("You can't restore this picture");
            } else {
                picture.setdeletedAt(null);

            }
        } else if (updateType == PictureUpdateType.UPDATE && this.canUserEdit(editorId, picture)) {
            if (picture.getdeletedAt() == null) {
                if (pictureUpdateDTO.getName() != null) {
                    picture.setName(pictureUpdateDTO.getName());
                }
            } else {
                throw new BadRequestException("You can't update this picture");
            }
        } else {
            System.out.println("No Update");
            return picture;
        }

        return this.pictureRepository.save(picture);
    }

    private boolean canUserEdit(UUID userId, Picture picture) {
        if (picture.getUser().getId().equals(userId)) {
            return true;
        }

        var permissions = picture.getPermissions();
        if (permissions != null) {
            return permissions.stream()
                    .anyMatch(permission ->
                            permission.getUser().getId().equals(userId) &&
                                    (permission.getType() == PicturePermissionType.CAN_EDIT ||
                                            permission.getType() == PicturePermissionType.ALL));
        }

        return false;
    }

    private boolean canUserDelete(UUID userId, Picture picture) {
        if (picture.getUser().getId().equals(userId)) {
            return true;
        }

        var permissions = picture.getPermissions();
        if (permissions != null) {
            return permissions.stream()
                    .anyMatch(permission ->
                            permission.getUser().getId().equals(userId) &&
                                    permission.getType() == PicturePermissionType.ALL);
        }
        return false;
    }

}
