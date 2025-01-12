package dev.ppondeu.java_starter.common.exceptions;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String message){
        super(message);
    }

    public ForbiddenException(){
        super("Forbidden");
    }
}
