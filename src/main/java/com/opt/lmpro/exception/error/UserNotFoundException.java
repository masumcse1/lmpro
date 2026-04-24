package com.opt.lmpro.exception.error;

import com.opt.lmpro.exception.global.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Exception for user lookup failures
 * Demonstrates simple resource-not-found pattern
 */
public class UserNotFoundException extends BusinessException {

    /**
     * Creates user not found exception with search context
     * @param userId The user ID that wasn't found
     */
    public UserNotFoundException(String userId) {
        super("User not found with ID: " + userId, "USER_NOT_FOUND", HttpStatus.NOT_FOUND);
        addContext("userId", userId);                  // Include failed lookup ID
        addContext("resourceType", "user");           // Specify resource type
    }
}