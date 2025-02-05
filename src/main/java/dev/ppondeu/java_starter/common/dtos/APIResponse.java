package dev.ppondeu.java_starter.common.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public record APIResponse<T>(int statusCode, String message, List<String> errors, T data) {

    // Method to convert the APIResponse to JSON
    public String toJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
