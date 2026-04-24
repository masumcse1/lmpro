package com.opt.lmpro.exception.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Standardized error response structure
 * Used by all exception handlers for consistency
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
public class ErrorResponse {
    private String code;                    // Machine-readable error code
    private String message;                 // Human-readable message
    private LocalDateTime timestamp;        // When error occurred
    private String path;                    // Request path that caused error
    private Map<String, Object> details;   // Additional context information

    /**
     * Builder method to add contextual details
     * @param key Detail key
     * @param value Detail value
     * @return Builder for method chaining
     */
    public static class ErrorResponseBuilder {
        public ErrorResponseBuilder addDetail(String key, Object value) {
            if (this.details == null) {
                this.details = new HashMap<>();
            }
            this.details.put(key, value);
            return this;
        }
    }
}