package dev.ppondeu.java_starter.pictures.interfaces;

import dev.ppondeu.java_starter.pictures.dtos.PictureCreateDTO;
import dev.ppondeu.java_starter.pictures.dtos.PictureUpdateDTO;
import dev.ppondeu.java_starter.pictures.entities.Picture;
import dev.ppondeu.java_starter.pictures.entities.PicturePermissionType;
import dev.ppondeu.java_starter.pictures.entities.PictureUpdateType;
import dev.ppondeu.java_starter.users.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IPictureService {
    public Picture createPicture(UUID userId, MultipartFile file);
    public List<Picture> getUserPictures(UUID userId);
    public Picture getPicture(UUID pictureId, UUID currentUserId);
    public void deletePicture(UUID pictureId, UUID currentUserId);
    public void softDeletePicture(UUID pictureId, UUID currentUserId);
    public Picture updatePictureDetails(UUID pictureId, PictureUpdateType updateType, UUID editorId, PictureUpdateDTO pictureUpdateDTO);
}
