package dev.ppondeu.java_starter.pictures.interfaces;

import dev.ppondeu.java_starter.pictures.entities.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IPictureService {
    public Picture createPicture(UUID userId, MultipartFile file);
}
