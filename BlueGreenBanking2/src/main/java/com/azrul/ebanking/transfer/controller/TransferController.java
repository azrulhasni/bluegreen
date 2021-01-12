/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.ebanking.transfer.controller;

import com.azrul.ebanking.transfer.domain.Account;
import com.azrul.ebanking.transfer.service.TransferService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author azrul
 */
@RestController
public class TransferController {
    
    @Autowired
    private TransferService transferService;
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error message")
    public void handleError() {
        
    }
    
    @PostMapping(path = "/transfer", produces = "application/json")
    public @ResponseBody Account doTransfer(@RequestParam String amount, 
            @RequestParam  String fromAccountNumber, 
            @RequestParam String toAccountNumber){
        Account account  = transferService.doTransfer(new BigDecimal(amount), fromAccountNumber, toAccountNumber).get();
        return account;
    }
    
    @GetMapping(path = "/gtransfer", produces = "application/json")
    public @ResponseBody Account doGetTransfer(@RequestParam String amount, 
            @RequestParam  String fromAccountNumber, 
            @RequestParam String toAccountNumber){
        Account account  = transferService.doTransfer(new BigDecimal(amount), fromAccountNumber, toAccountNumber).get();
        return account;
    }
    
}
