package dev.ppondeu.java_starter.common.exceptions;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException() {
        super("Internal Server Error");
    }

}
