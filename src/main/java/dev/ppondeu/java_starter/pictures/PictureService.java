package dev.ppondeu.java_starter.pictures;

import dev.ppondeu.java_starter.common.exceptions.BadRequestException;
import dev.ppondeu.java_starter.common.exceptions.NotFoundException;
import dev.ppondeu.java_starter.common.interfaces.IFileStorageService;
import dev.ppondeu.java_starter.pictures.dtos.PictureCreateDTO;
import dev.ppondeu.java_starter.pictures.entities.Picture;
import dev.ppondeu.java_starter.pictures.entities.PicturePermission;
import dev.ppondeu.java_starter.pictures.entities.PicturePermissionType;
import dev.ppondeu.java_starter.pictures.interfaces.IPicturePermissionRepository;
import dev.ppondeu.java_starter.pictures.interfaces.IPictureRepository;
import dev.ppondeu.java_starter.pictures.interfaces.IPictureService;
import dev.ppondeu.java_starter.pictures.mappers.PictureMapper;
import dev.ppondeu.java_starter.users.interfaces.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        var picturePermission = PictureMapper.mapToPicturePermission(user, picture, PicturePermissionType.DELETE);
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

}
