package dev.ppondeu.java_starter.common.dtos;

import java.util.List;

public record APIResponse<T> (int statusCode, String message, List<String> errors, T data) {

}
