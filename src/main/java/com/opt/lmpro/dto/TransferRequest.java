package com.opt.lmpro.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    private String fromUserId;
    private String toUserId;
    private BigDecimal amount;
}