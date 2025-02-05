package dev.ppondeu.java_starter.pictures.mappers;

import dev.ppondeu.java_starter.pictures.dtos.PictureCreateDTO;
import dev.ppondeu.java_starter.pictures.entities.Picture;
import dev.ppondeu.java_starter.pictures.entities.PicturePermission;
import dev.ppondeu.java_starter.pictures.entities.PicturePermissionType;
import dev.ppondeu.java_starter.users.entities.User;

public class PictureMapper {
    public static Picture mapToPicture(PictureCreateDTO pictureCreateDTO, User user) {
        return new Picture(
                pictureCreateDTO,
                user
        );
    }

    public static PicturePermission mapToPicturePermission(User user, Picture picture, PicturePermissionType type) {
        return new PicturePermission(
                user,
                picture,
                type
        );
    }

}
