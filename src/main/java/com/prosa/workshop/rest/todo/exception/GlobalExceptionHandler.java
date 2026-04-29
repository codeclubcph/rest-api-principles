package com.prosa.workshop.rest.todo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // -------------------------------------------------------------------------
    // TODO G — Handle ResourceNotFoundException
    // -------------------------------------------------------------------------
    // When a todo is not found, return 404 NOT_FOUND with an ErrorResponse.
    //
    // Hint:
    //   return ResponseEntity.status(HttpStatus.NOT_FOUND)
    //       .body(new ErrorResponse("NOT_FOUND", ex.getMessage()));
    // -------------------------------------------------------------------------
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("NOT_FOUND", ex.getMessage()));
    }

    // -------------------------------------------------------------------------
    // TODO H — Handle MethodArgumentNotValidException
    // -------------------------------------------------------------------------
    // When @Valid fails on a request body, return 400 BAD_REQUEST with a
    // VALIDATION_ERROR response that lists all field errors in the message.
    //
    // Hint: ex.getBindingResult().getFieldErrors() gives you the list of errors.
    //       Each FieldError has .getField() and .getDefaultMessage().
    //       Join them: "title: Title is required, dueDate: must not be null"
    // -------------------------------------------------------------------------
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDATION_ERROR", "Request body is missing or malformed"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDATION_ERROR", message));
    }

    // -------------------------------------------------------------------------
    // TODO I — Handle all other exceptions (catch-all)
    // -------------------------------------------------------------------------
    // For any unexpected error, return 500 INTERNAL_SERVER_ERROR.
    // IMPORTANT: log the full exception server-side, but return a safe generic
    // message to the client — never expose internal stack traces!
    //
    // Hint: add a Logger field:
    //   private static final org.slf4j.Logger log =
    //       org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);
    //   log.error("Unexpected error", ex);
    // -------------------------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"));
    }
}
