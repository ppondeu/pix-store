package dev.ppondeu.java_starter.common.filters;

import dev.ppondeu.java_starter.common.dtos.APIResponse;
import dev.ppondeu.java_starter.common.exceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalFilterException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        System.out.println(ex.getMessage());
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        // Construct the API response
        APIResponse<?> apiResponse = new APIResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors,
                null // No data to return in this case
        );

        // Return the response with BAD_REQUEST status
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIResponse<?>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String errorMessage = "Database error: " + ex.getMessage();

        // Extract specific details about the violation, if available
        String[] errorParts = ex.getMostSpecificCause().getMessage().split(":");
        String violationDetails = errorParts.length > 1 ? errorParts[1] : "Unknown database violation";

        APIResponse<?> apiResponse = new APIResponse<>(
                HttpStatus.CONFLICT.value(),
                "Data integrity violation",
                List.of(violationDetails),
                null
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<APIResponse<?>> handleInterruptedException(InternalServerException ex) {
        String message = ex.getMessage();
        APIResponse<?> response = new APIResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                List.of(message),
                null
        );

        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIResponse<?>> handleBadRequestException(BadRequestException ex) {
        String message = ex.getMessage();
        APIResponse<?> response = new APIResponse<>(
                HttpStatus.BAD_REQUEST.value(), // HTTP Status code 500
                "Bad Request",
                List.of(message),
                null
        );

        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<APIResponse<?>> handleBUnauthorizedException(UnauthorizedException ex) {
        String message = ex.getMessage();
        APIResponse<?> response = new APIResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                List.of(message),
                null
        );

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<APIResponse<?>> handleForbiddenException(ForbiddenException ex) {
        String message = ex.getMessage();
        APIResponse<?> response = new APIResponse<>(
                HttpStatus.FORBIDDEN.value(), // HTTP Status code 500
                "Forbidden",
                List.of(message),
                null
        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<APIResponse<?>> handleNotFoundException(NotFoundException ex) {
        String message = ex.getMessage();
        APIResponse<?> response = new APIResponse<>(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                List.of(message),
                null
        );

        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<APIResponse<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("Invalid value for parameter '%s'. Expected type: %s",
                ex.getName(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        APIResponse<?> apiResponse = new APIResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Type Parameter",
                Collections.singletonList(errorMessage),
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<APIResponse<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {

        APIResponse<?> apiResponse = new APIResponse<>(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Not Allowed",
                Collections.singletonList(ex.getMessage()),
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<APIResponse<?>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {

        APIResponse<?> apiResponse = new APIResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Http Media Type Not Supported",
                Collections.singletonList(ex.getMessage()),
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<APIResponse<?>> handleExpiredJwtException(Exception ex) {
        APIResponse<?> apiResponse = new APIResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                Collections.singletonList(ex.getMessage()),
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<APIResponse<?>> handleNoResourceFoundExceptionException(NoResourceFoundException ex) {

        APIResponse<?> apiResponse = new APIResponse<>(
                HttpStatus.NOT_FOUND.value(),
                "No Resource Found",
                Collections.singletonList(ex.getMessage()),
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<APIResponse<?>> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {

        APIResponse<?> apiResponse = new APIResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Missing Servlet Request Part",
                Collections.singletonList(ex.getMessage()),
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<?>> handleGenericException(Exception ex) {
        System.out.println("Test Test");
        APIResponse<?> apiResponse = new APIResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                Collections.singletonList(ex.getMessage()),
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
