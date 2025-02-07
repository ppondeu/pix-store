package dev.ppondeu.java_starter.pictures.interfaces;

import dev.ppondeu.java_starter.pictures.entities.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IPictureService {
    public Picture createPicture(UUID userId, MultipartFile file);
    public List<Picture> getUserPictures(UUID userId);
    public Picture getPicture(UUID pictureId, UUID currentUserId);
}
