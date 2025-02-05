package dev.ppondeu.java_starter.pictures.services;

import dev.ppondeu.java_starter.pictures.dtos.PictureCreateDTO;
import dev.ppondeu.java_starter.pictures.entities.Picture;
import dev.ppondeu.java_starter.pictures.interfaces.IPictureRepository;
import dev.ppondeu.java_starter.pictures.interfaces.IPictureService;
import dev.ppondeu.java_starter.pictures.mappers.PictureMapper;
import dev.ppondeu.java_starter.users.interfaces.IUserService;

public class PictureService implements IPictureService {

    private final IPictureRepository pictureRepository;
    private final IUserService userService;

    public PictureService(
            IPictureRepository pictureRepository,
            IUserService userService
    ) {
        this.pictureRepository = pictureRepository;
        this.userService = userService;
    }

    @Override
    public Picture createPicture(PictureCreateDTO pictureCreateDTO) {
        var user = this.userService.getUserById(pictureCreateDTO.getUserId());
        var picture = PictureMapper.mapToPicture(pictureCreateDTO, user);
    }
}
