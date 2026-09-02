package com.opt.lmpro.exception.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
public enum ErrorType {

    UNKNOWN_ERROR("5000", "Unknown error !!!",HttpStatus.PAYMENT_REQUIRED),

    INSUFFICIENT_FUNDS("6001","Transaction declined: Account balance is below the required amount.",HttpStatus.PAYMENT_REQUIRED),

    MISSING_PACKAGE_DESCRIPTION("7002", "Required field vacation 'description' is missing",HttpStatus.PAYMENT_REQUIRED);


    private final String errorCode;
    private final String message;
    private final HttpStatus status;

}