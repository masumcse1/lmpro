package com.opt.lmpro.exception.global;

import com.opt.lmpro.exception.error.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Global exception handler - Foundation of professional error handling
 * Provides consistent error responses across the entire application
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles business logic exceptions with appropriate HTTP status
     * @param ex Business exception containing error context
     * @return Structured error response with NOT_FOUND status
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        // Log business errors at WARN level - expected failures
       // log.warn("User not found: {}", ex.getUserId());

        ErrorResponse error = ErrorResponse.builder()
                .code("USER_NOT_FOUND")           // Machine-readable code
                .message(ex.getMessage())         // Human-readable message
                .timestamp(LocalDateTime.now())  // When error occurred
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Catch-all handler for unexpected errors
     * Generates tracking ID and prevents information leakage
     * @param ex Any unhandled exception
     * @return Generic error response with tracking reference
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        // Generate unique ID for error tracking and correlation
        String errorId = UUID.randomUUID().toString();

        // Log unexpected errors at ERROR level with full context
        log.error("Unexpected error [{}]", errorId, ex);

        ErrorResponse error = ErrorResponse.builder()
                .code("INTERNAL_ERROR")
                .message("An unexpected error occurred. Error ID: " + errorId)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Handler for all business exceptions
     * Processes exceptions uniformly while preserving individual characteristics
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {

        // Create response with exception's own error code and message
        ErrorResponse response = ErrorResponse.builder()
                .code(ex.getErrorCode())              // Use exception's error code
                .message(ex.getMessage())             // Use exception's message
                .timestamp(LocalDateTime.now())
                .details(ex.getContext())             // Include rich context
                .build();

        // Log with appropriate level based on HTTP status
        if (ex.getHttpStatus().is5xxServerError()) {
            log.error("Business exception: {}", ex.getMessage(), ex);
        } else {
            log.warn("Business exception: {} - {}", ex.getErrorCode(), ex.getMessage());
        }

        // Return response with exception's designated HTTP status
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }
}
