package dev.ppondeu.java_starter.pictures;

import dev.ppondeu.java_starter.common.interfaces.IFileStorageService;
import dev.ppondeu.java_starter.pictures.dtos.PictureCreateDTO;
import dev.ppondeu.java_starter.pictures.entities.Picture;
import dev.ppondeu.java_starter.pictures.entities.PicturePermissionType;
import dev.ppondeu.java_starter.pictures.interfaces.IPicturePermissionRepository;
import dev.ppondeu.java_starter.pictures.interfaces.IPictureRepository;
import dev.ppondeu.java_starter.pictures.interfaces.IPictureService;
import dev.ppondeu.java_starter.pictures.mappers.PictureMapper;
import dev.ppondeu.java_starter.users.interfaces.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

//        handle file storage service here
//        setFilePath
        String filePath = this.fileStorageService.store(file);
        pictureCreateDTO.setFilePath(filePath);
        System.out.println(pictureCreateDTO.getFilePath());
        var picture = PictureMapper.mapToPicture(pictureCreateDTO, user);
        var newPicture = this.pictureRepository.save(picture);
        var picturePermission = PictureMapper.mapToPicturePermission(user, picture, PicturePermissionType.DELETE);
        this.picturePermissionRepository.save(picturePermission);
        return newPicture;
    }

}
