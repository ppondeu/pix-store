package dev.ppondeu.java_starter.common.services;

import dev.ppondeu.java_starter.common.exceptions.BadRequestException;
import dev.ppondeu.java_starter.common.exceptions.NotFoundException;
import dev.ppondeu.java_starter.common.interfaces.IFileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService implements IFileStorageService {
    private final Path uploadDirPath;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        if (uploadDir == null) {
            throw new RuntimeException("UploadDir is null");
        } else if (uploadDir.trim().isEmpty()) {
            throw new RuntimeException("UploadDir is empty");
        }
        this.uploadDirPath = Paths.get(uploadDir);
    }

    @Override
    public void init() {
        try {
            if (!Files.exists(this.uploadDirPath)) {
                Files.createDirectory(this.uploadDirPath);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            String originalExtension = FileStorageService.getFileExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + originalExtension;

            Path destinationFile = this.uploadDirPath
                    .resolve(fileName)
                    .normalize()
                    .toAbsolutePath();
            System.out.println("Storing file: " + destinationFile);
            System.out.println("Original extension: " + this.uploadDirPath);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("Stored file: " + fileName);
            return fileName;

        } catch (IOException e) {
            throw new BadRequestException("Failed to store file: " + file.getOriginalFilename());
        }
    }

    @Override
    public Resource loadAsResource(String fileName) {
        try {
            Path file = this.uploadDirPath.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new NotFoundException("File not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String getFileExtension(String fileName) {
        if (fileName != null && fileName.lastIndexOf('.') > 0) {
            return fileName.substring(fileName.lastIndexOf('.'));
        }
        return "";
    }
}
