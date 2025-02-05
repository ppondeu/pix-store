package dev.ppondeu.java_starter.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public class PictureCreateDTO {
    @NotBlank(message = "Picture Name is required")
    @Length(min = 1, max = 128, message = "Picture Name length must be between 1 and 128")
    private final String name;

//    @NotBlank(message = "File Path is required")
    private String filePath;

//    @NotBlank(message = "User ID is required")
//    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
//            message = "Invalid User ID format. It must be a valid UUID.")
    private UUID userId;

    public PictureCreateDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setFilePath(@NotBlank(message = "File Path is required") String filePath) {
        this.filePath = filePath;
    }

    public void setUserId(@NotBlank(message = "User ID is required") @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Invalid User ID format. It must be a valid UUID.") UUID userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PictureCreateDTO{" +
                "name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                ", userId=" + userId +
                '}';
    }
}
