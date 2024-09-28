package org.consoleconnect.spring_react_user_registration.exceptions;

import static java.util.Objects.nonNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {

        return buildErrorResponse(exception, HttpStatus.NOT_FOUND, request, "/errors/user-not-found", null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request, "/errors/validation-error", errors);
    }

    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ResponseEntity<Object> handleUniqueConstraintViolationException(UniqueConstraintViolationException ex, WebRequest request) {

        return buildErrorResponse(ex, HttpStatus.CONFLICT, request, "/errors/unique-constraint-violation", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception exception) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, HttpStatus httpStatus, WebRequest request, String type, Map<String, String> errors) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", type);
        body.put("title", httpStatus.getReasonPhrase());
        body.put("status", httpStatus.value());
        body.put("detail", exception.getMessage());
        body.put("instance", request.getDescription(false).substring(4));

        if (nonNull(errors) && !errors.isEmpty()) {
            body.put("errors", errors);
        }

        return new ResponseEntity<>(body, httpStatus);
    }
}