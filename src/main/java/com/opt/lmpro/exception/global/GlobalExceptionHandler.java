package com.opt.lmpro.exception.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Produces one standard error payload for all handled exceptions.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {

        Error_Type errorType = resolveErrorType(ex.getErrorCode());
        String traceId = getTraceId();
        Map<String, Object> context = ex.getContext();

        ErrorResponse response = baseResponseBuilder(request, traceId)
                .code(errorType.getCode())
                .errorType(ex.getErrorCode())
                .message(ex.getMessage())
                .details(context.isEmpty() ? null : context)
                .build();

        if (ex.getHttpStatus().is5xxServerError()) {
            log.error("Business exception traceId={} errorType={} message={}",
                    traceId, ex.getErrorCode(), ex.getMessage(), ex);
        } else {
            log.warn("Business exception traceId={} errorType={} message={}",
                    traceId, ex.getErrorCode(), ex.getMessage());
        }

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String traceId = getTraceId();
        Map<String, Object> details = new LinkedHashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            details.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        log.warn("Validation failed traceId={} path={} errors={}",
                traceId, request.getRequestURI(), details);

        ErrorResponse response = baseResponseBuilder(request, traceId)
                .code(Error_Type.VALIDATION_ERROR.getCode())
                .errorType(Error_Type.VALIDATION_ERROR.name())
                .message(Error_Type.VALIDATION_ERROR.getMessage())
                .details(details)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            Exception ex,
            HttpServletRequest request) {

        String traceId = getTraceId();
        Map<String, Object> details = Map.of("reason", ex.getMessage());

        log.warn("Bad request traceId={} path={} reason={}",
                traceId, request.getRequestURI(), ex.getMessage());

        ErrorResponse response = baseResponseBuilder(request, traceId)
                .code(Error_Type.INVALID_TRANSFER_REQUEST.getCode())
                .errorType(Error_Type.INVALID_TRANSFER_REQUEST.name())
                .message(Error_Type.INVALID_TRANSFER_REQUEST.getMessage())
                .details(details)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex,
            HttpServletRequest request) {

        String traceId = getTraceId();

        log.error("Unexpected error traceId={} path={}", traceId, request.getRequestURI(), ex);

        ErrorResponse response = baseResponseBuilder(request, traceId)
                .code(Error_Type.UNKNOWN_ERROR.getCode())
                .errorType(Error_Type.UNKNOWN_ERROR.name())
                .message("An unexpected error occurred")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private Error_Type resolveErrorType(String errorCode) {
        try {
            return Error_Type.valueOf(errorCode);
        } catch (IllegalArgumentException ignored) {
            return Error_Type.UNKNOWN_ERROR;
        }
    }

    private ErrorResponse.ErrorResponseBuilder baseResponseBuilder(
            HttpServletRequest request,
            String traceId) {

        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .method(request.getMethod())
                .path(request.getRequestURI())
                .traceId(traceId);
    }

    private String getTraceId() {
        String traceId = MDC.get("traceId");
        return traceId != null ? traceId : "N/A";
    }
}
