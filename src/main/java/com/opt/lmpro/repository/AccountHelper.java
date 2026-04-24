package com.opt.lmpro.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AccountHelper {

    // userId → accountId
    public static final Map<String, String> USER_ACCOUNT = new HashMap<>();

    // accountId → balance
    public static final Map<String, BigDecimal> ACCOUNTS = new HashMap<>();

    static {
        USER_ACCOUNT.put("U1", "A1");
        USER_ACCOUNT.put("U2", "A2");

        ACCOUNTS.put("A1", new BigDecimal("1000"));
        ACCOUNTS.put("A2", new BigDecimal("500"));
    }
}