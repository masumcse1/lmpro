package com.opt.lmpro.exception.error;

import com.opt.lmpro.exception.global.BusinessException;
import com.opt.lmpro.exception.global.ErrorType;
import java.math.BigDecimal;

/**
 * Exception for insufficient funds scenarios
 * Demonstrates rich business context with financial details
 */
public class InsufficientFundsException extends BusinessException {

    public InsufficientFundsException(ErrorType error, String accountId, BigDecimal requested, BigDecimal available) {

        super(error.getErrorCode(), error.getMessage(), error.getStatus());

        addContext("accountId", accountId);
        addContext("requestedAmount", requested);
        addContext("availableAmount", available);
        addContext("shortfall", requested.subtract(available));
    }
}