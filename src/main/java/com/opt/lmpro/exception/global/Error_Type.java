package com.opt.lmpro.exception.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Use 4 digit error code

Code = 5000 Unknown Error
Code = 6xxx Any database error
Code = 7xxx Validation errors
*/

/*
ACME Error Messages

https://developers.acmeticketing.com/support/solutions/articles/33000248662-api-response-codes-and-error-messages

*/

@Getter
@AllArgsConstructor
public enum Error_Type {

    UNKNOWN_ERROR(5000, "Unknown error !!!"),
    USER_NOT_FOUND(4004, "User not found."),

    INSUFFICIENT_FUNDS(6001,"Transaction declined: Account balance is below the required amount."),
    INVALID_TRANSFER_REQUEST(7001, "Transfer request is invalid."),

    MISSING_PACKAGE_DESCRIPTION(7002, "Required field vacation 'description' is missing"),
    VALIDATION_ERROR(7003, "Request validation failed.");


    private final int code;
    private final String message;

}
