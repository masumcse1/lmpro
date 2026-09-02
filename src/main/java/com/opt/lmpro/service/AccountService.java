package com.opt.lmpro.service;

import com.opt.lmpro.dto.TransferResult;
import com.opt.lmpro.exception.error.InsufficientFundsException;
import com.opt.lmpro.exception.error.UserNotFoundException;
import com.opt.lmpro.exception.global.ErrorType;
import com.opt.lmpro.repository.AccountHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    public TransferResult transferFunds(String fromUserId, String toUserId, BigDecimal amount) {

        String fromAcc = AccountHelper.USER_ACCOUNT.get(fromUserId);
        String toAcc = AccountHelper.USER_ACCOUNT.get(toUserId);

        if (fromAcc == null) {
            throw new UserNotFoundException(fromUserId);
        }

        if (toAcc == null) {
            throw new UserNotFoundException(toUserId);
        }

        BigDecimal fromBalance = AccountHelper.ACCOUNTS.get(fromAcc);
        BigDecimal toBalance = AccountHelper.ACCOUNTS.get(toAcc);

        if (fromBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(ErrorType.INSUFFICIENT_FUNDS,fromAcc,amount,fromBalance);
        }

        // update balances
        AccountHelper.ACCOUNTS.put(fromAcc, fromBalance.subtract(amount));
        AccountHelper.ACCOUNTS.put(toAcc, toBalance.add(amount));

        return new TransferResult(fromAcc, toAcc, amount);
    }
}