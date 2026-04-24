package com.opt.lmpro.web;

import com.opt.lmpro.dto.TransferRequest;
import com.opt.lmpro.dto.TransferResult;
import com.opt.lmpro.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/transfer")
    public TransferResult transfer(@RequestBody TransferRequest req) {
        return accountService.transferFunds(
                req.getFromUserId(),
                req.getToUserId(),
                req.getAmount()
        );
    }
}