package dev.ppondeu.java_starter.common.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super("Not Found");
    }
}
