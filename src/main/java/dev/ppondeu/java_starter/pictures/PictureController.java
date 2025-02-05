package dev.ppondeu.java_starter.pictures;

import dev.ppondeu.java_starter.pictures.entities.Picture;
import dev.ppondeu.java_starter.pictures.interfaces.IPictureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/pictures", produces = "application/json")
public class PictureController {
    private final IPictureService pictureService;

    public PictureController(IPictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping(path = "")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping(path = "", consumes = "multipart/form-data")
    public ResponseEntity<Picture> createPicture(
            @RequestParam("userId") UUID userId,
            @RequestParam("image") MultipartFile file
            ){
        System.out.println(file.getOriginalFilename());
        System.out.println(userId);
        var picture = this.pictureService.createPicture(userId, file);
        return ResponseEntity.ok(picture);
    }
}
