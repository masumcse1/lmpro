package com.opt.lmpro.exception.error;

import com.opt.lmpro.exception.global.BusinessException;
import com.opt.lmpro.exception.global.Error_Type;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

/**
 * Exception for insufficient funds scenarios
 * Demonstrates rich business context with financial details
 */
public class InsufficientFundsException extends BusinessException {

    public InsufficientFundsException(String accountId, BigDecimal requested, BigDecimal available) {
        super(Error_Type.INSUFFICIENT_FUNDS.name(),Error_Type.INSUFFICIENT_FUNDS.getMessage(),HttpStatus.PAYMENT_REQUIRED);

        addContext("accountId", accountId);
        addContext("requestedAmount", requested);
        addContext("availableAmount", available);
        addContext("shortfall", requested.subtract(available));
    }
}