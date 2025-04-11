package dev.ppondeu.java_starter.pictures.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class PictureUpdateDTO {
    @Length(min = 1, max = 128, message = "Picture Name length must be between 1 and 128")
    private final String name;

    public PictureUpdateDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
