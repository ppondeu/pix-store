package dev.ppondeu.java_starter.pictures;

import dev.ppondeu.java_starter.common.dtos.APIResponse;
import dev.ppondeu.java_starter.common.exceptions.UnauthorizedException;
import dev.ppondeu.java_starter.pictures.dtos.PictureUpdateDTO;
import dev.ppondeu.java_starter.pictures.entities.Picture;
import dev.ppondeu.java_starter.pictures.entities.PictureUpdateType;
import dev.ppondeu.java_starter.pictures.interfaces.IPictureService;
import dev.ppondeu.java_starter.users.dtos.UserUpdateDTO;
import dev.ppondeu.java_starter.users.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    @DeleteMapping("{id}")
    public ResponseEntity<APIResponse<?>> deletePicture(HttpServletRequest request, @PathVariable UUID id) {
        var user = (User) request.getAttribute("user");
        if (user == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        pictureService.deletePicture(id, user.getId());
        var apiResponse = new APIResponse<String>(
                HttpStatus.OK.value(),
                "delete picture with id#" + id + " successfully.",
                Collections.emptyList(),
                null
        );

        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("{id}")
    public ResponseEntity<APIResponse<Picture>> updatePictureDetail(HttpServletRequest request, @PathVariable UUID id, @Validated @RequestBody PictureUpdateDTO pictureUpdateDTO) {
        System.out.println("update picture");
        System.out.println(pictureUpdateDTO.getName());
        var user = (User) request.getAttribute("user");
        if (user == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        var picture = this.pictureService.updatePictureDetails(id, PictureUpdateType.UPDATE, user.getId(), pictureUpdateDTO);
        var apiResponse = new APIResponse<Picture>(
                HttpStatus.OK.value(),
                "picture update successfully.",
                Collections.emptyList(),
                picture
        );

        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("{id}/soft-delete")
    public ResponseEntity<APIResponse<Picture>> softDeletePicture(HttpServletRequest request, @PathVariable UUID id) {
        System.out.println("Soft Delete");
        var user = (User) request.getAttribute("user");
        if (user == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        var picture = this.pictureService.updatePictureDetails(id, PictureUpdateType.DELETE, user.getId(), null);
        var apiResponse = new APIResponse<Picture>(
                HttpStatus.OK.value(),
                "picture update successfully.",
                Collections.emptyList(),
                picture
        );

        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("{id}/restore")
    public ResponseEntity<APIResponse<Picture>> restorePicture(HttpServletRequest request, @PathVariable UUID id) {
        System.out.println("Restore");
        var user = (User) request.getAttribute("user");
        if (user == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        var picture = this.pictureService.updatePictureDetails(id, PictureUpdateType.RESTORE, user.getId(), null);
        var apiResponse = new APIResponse<Picture>(
                HttpStatus.OK.value(),
                "picture restore successfully.",
                Collections.emptyList(),
                picture
        );

        return ResponseEntity.ok(apiResponse);
    }
}
