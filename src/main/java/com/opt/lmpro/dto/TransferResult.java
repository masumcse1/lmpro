package com.opt.lmpro.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransferResult {
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
}