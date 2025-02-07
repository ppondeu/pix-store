package dev.ppondeu.java_starter.pictures;

import dev.ppondeu.java_starter.common.dtos.APIResponse;
import dev.ppondeu.java_starter.common.exceptions.UnauthorizedException;
import dev.ppondeu.java_starter.pictures.entities.Picture;
import dev.ppondeu.java_starter.pictures.interfaces.IPictureService;
import dev.ppondeu.java_starter.users.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/pictures", produces = "application/json")
public class PictureController {
    private final IPictureService pictureService;

    public PictureController(IPictureService pictureService) {
        this.pictureService = pictureService;
    }

    @PostMapping(path = "", consumes = "multipart/form-data")
    public ResponseEntity<APIResponse<Picture>> createPicture(
            HttpServletRequest request,
            @RequestParam("image") MultipartFile file
            ){
        var user = (User) request.getAttribute("user");
        if (user == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        System.out.println(file.getOriginalFilename());
        System.out.println(user);
        var picture = this.pictureService.createPicture(user.getId(), file);
        var apiResponse = new APIResponse<Picture>(
                HttpStatus.CREATED.value(),
                "create picture successfully.",
                Collections.emptyList(),
                picture
        );
        return new ResponseEntity<APIResponse<Picture>>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<APIResponse<List<Picture>>> getPictures(HttpServletRequest request) {
        var user = (User) request.getAttribute("user");
        if (user == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        var result = pictureService.getUserPictures(user.getId());
        var apiResponse = new APIResponse<List<Picture>>(
                HttpStatus.OK.value(),
                "get user pictures successfully.",
                Collections.emptyList(),
                result
        );
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<APIResponse<Picture>> getPicture(HttpServletRequest request, @PathVariable UUID id) {
        var user = (User) request.getAttribute("user");
        if (user == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        var result = pictureService.getPicture(id, user.getId());
        var apiResponse = new APIResponse<Picture>(
                HttpStatus.OK.value(),
                "get picture successfully.",
                Collections.emptyList(),
                result
        );

        return ResponseEntity.ok(apiResponse);
    }
}
