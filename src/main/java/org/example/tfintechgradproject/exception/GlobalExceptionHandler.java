package org.example.tfintechgradproject.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.tfintechgradproject.exception.exceptions.CannotJoinActivityRequestException;
import org.example.tfintechgradproject.exception.exceptions.ExternalServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException handler: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getBindingResult().getAllErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> ((FieldError)error).getField(),
                        error -> Objects.requireNonNullElse(error.getDefaultMessage(), ""))
                )
        );
    }

    @ExceptionHandler({ ExternalServiceUnavailableException.class })
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<String> handleExternalServiceUnavailable(ExternalServiceUnavailableException e) {
        log.warn("ExternalServiceUnavailableException handler: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("Retry-After", "60s")
                .body(e.getMessage());
    }

    @ExceptionHandler({ CannotJoinActivityRequestException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleCannotJoinActivityRequest(CannotJoinActivityRequestException e) {
        log.warn("CannotJoinActivityRequestException handler: {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("EntityNotFoundException handler: {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler({ IllegalStateException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalStateException(IllegalStateException e) {
        log.warn("IllegalStateException handler: {}", e.getMessage());
        return e.getMessage();
    }
}
