package dev.ppondeu.java_starter.attachments;

import dev.ppondeu.java_starter.common.services.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping(path = "/attachments")
public class AttachmentController {

    private final FileStorageService fileStorageService;

    public AttachmentController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping(path = "/{file-name}")
    public ResponseEntity<Resource> serveFile(@PathVariable("file-name") String fileName) {
        var fileServed = this.fileStorageService.loadAsResource(fileName);
        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(fileServed.getFile().toPath()); // Automatically determine content type
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(contentType);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
                .body(fileServed);
    }

    @GetMapping(path = "/downloads/{file-name}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("file-name") String fileName) {
        var fileServed = this.fileStorageService.loadAsResource(fileName);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(fileServed);
    }
}
