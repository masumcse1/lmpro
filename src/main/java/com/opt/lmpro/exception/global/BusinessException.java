package com.opt.lmpro.exception.global;

import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all business exceptions
 * Provides consistent structure, HTTP status mapping, and context management
 */
public abstract class BusinessException extends RuntimeException {

    private final String errorCode;                    // Machine-readable error identifier
    private final HttpStatus httpStatus;               // Appropriate HTTP response status
    private final Map<String, Object> context = new HashMap<>();  // Additional error context

    /**
     * Constructor for business exceptions
     * @param message Human-readable error description
     * @param errorCode Machine-readable error code (e.g., "USER_NOT_FOUND")
     * @param httpStatus HTTP status to return (e.g., HttpStatus.NOT_FOUND)
     */
    protected BusinessException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    // Getters for exception properties
    public String getErrorCode() { return errorCode; }
    public HttpStatus getHttpStatus() { return httpStatus; }

    /**
     * Add contextual information to the exception
     * Enables rich error details without changing constructor signatures
     * @param key Context key
     * @param value Context value
     * @return This exception for method chaining
     */
    public BusinessException addContext(String key, Object value) {
        context.put(key, value);
        return this;
    }

    /**
     * Get copy of context to prevent external modification
     * @return Immutable copy of context data
     */
    public Map<String, Object> getContext() {
        return new HashMap<>(context);
    }
}