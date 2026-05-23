package com.opt.lmpro.exception.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Standardized error response structure
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private Instant timestamp;            // ISO-8601 UTC (Z)
    private String code;
    private String error_type;
    private String message;
    private String method;                // HTTP method
    private String path;                  // API path
    private String traceId;               // tracking id
    private Map<String, Object> details;

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