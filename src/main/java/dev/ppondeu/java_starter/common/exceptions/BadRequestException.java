package dev.ppondeu.java_starter.common.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException() {
        super("Bad Request");
    }

}

