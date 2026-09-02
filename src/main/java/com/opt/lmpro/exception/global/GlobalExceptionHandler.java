package com.opt.lmpro.exception.global;

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

        log.error("Unexpected error [{}] service={} class={} method={} httpMethod={} path={} errorCode={} message={} context={}",
                traceId,
                "EmailSenderService",
                "GlobalExceptionHandler",
                "handleGeneral",
                request.getMethod(),
                request.getRequestURI(),
                "INTERNAL_ERROR",
                "An unexpected error occurred",
                null,
                ex
        );

        ErrorResponse error = ErrorResponse.builder()
                .code("INTERNAL_ERROR")
                .errorType("INTERNAL_ERROR")
                .message("An unexpected error occurred")
                .timestamp(Instant.now())
                .method(request.getMethod())
                .path(request.getRequestURI())
                .traceId(traceId)
                .details(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();

        log.warn("BusinessException traceId={} service={} class={} method={} httpMethod={} path={} errorCode={} message={} context={}",
                traceId,
                "EmailSenderService",
                "GlobalExceptionHandler",
                "handleBusinessException",
                request.getMethod(),
                request.getRequestURI(),
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getContext(),
                ex
        );

        ErrorResponse response = ErrorResponse.builder()
                .code(ex.getErrorCode())
                .errorType(ex.getErrorCode())
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .method(request.getMethod())
                .path(request.getRequestURI())
                .traceId(traceId)
                .details(ex.getContext())
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }
}
