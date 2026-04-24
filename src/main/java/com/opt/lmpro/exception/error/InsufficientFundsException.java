package com.opt.lmpro.exception.error;

import com.opt.lmpro.exception.global.BusinessException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

/**
 * Exception for insufficient funds scenarios
 * Demonstrates rich business context with financial details
 */
public class InsufficientFundsException extends BusinessException {

    /**
     * Creates insufficient funds exception with financial context
     * @param accountId Account that lacks sufficient funds
     * @param requested Amount requested for transaction
     * @param available Current available balance
     */
    public InsufficientFundsException(String accountId, BigDecimal requested, BigDecimal available) {
        super("Insufficient funds in account: " + accountId,
                "INSUFFICIENT_FUNDS", HttpStatus.PAYMENT_REQUIRED);

        // Add rich financial context for client decision-making
        addContext("accountId", accountId);
        addContext("requestedAmount", requested);
        addContext("availableAmount", available);
        addContext("shortfall", requested.subtract(available));    // Calculate shortfall
    }
}
