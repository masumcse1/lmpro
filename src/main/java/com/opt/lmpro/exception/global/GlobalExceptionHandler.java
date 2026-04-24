package com.opt.lmpro.exception.global;

import com.opt.lmpro.exception.error.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
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
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();

        ErrorResponse error = ErrorResponse.builder()
                .error_type("USER_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .method(request.getMethod())
                .path(request.getRequestURI())
                .traceId(traceId)
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
    public ResponseEntity<ErrorResponse> handleGeneral(
            Exception ex,
            HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();

        log.error("Unexpected error [{}]", traceId, ex);

        ErrorResponse error = ErrorResponse.builder()
                .error_type("INTERNAL_ERROR")
                .message("An unexpected error occurred")
                .timestamp(Instant.now())
                .method(request.getMethod())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();

        ErrorResponse response = ErrorResponse.builder()
                .code(Error_Type.valueOf(ex.getErrorCode()).getCode())
                .error_type(ex.getErrorCode())
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .method(request.getMethod())
                .path(request.getRequestURI())
                .traceId(traceId)
                .details(ex.getContext())
                .build();

        if (ex.getHttpStatus().is5xxServerError()) {
            log.error("Business exception [{}]: {}", traceId, ex.getMessage(), ex);
        } else {
            log.warn("Business exception [{}]: {} - {}", traceId, ex.getErrorCode(), ex.getMessage());
        }

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }
}
