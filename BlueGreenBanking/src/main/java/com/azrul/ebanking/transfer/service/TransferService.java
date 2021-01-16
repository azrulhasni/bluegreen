/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.ebanking.transfer.service;

import com.azrul.ebanking.transfer.domain.Account;
import com.azrul.ebanking.transfer.domain.Transaction;
import com.azrul.ebanking.transfer.exception.TransactionException;
import com.azrul.ebanking.transfer.repository.AccountRepository;
import com.azrul.ebanking.transfer.repository.TransactionRepository;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author azrul
 */
@Service
public class TransferService {
    
    @Autowired
    private AccountRepository accountRepo;
    
    @Autowired
    private TransactionRepository trxRepo;
    
    @Transactional
    public Optional<Account> doTransfer(BigDecimal amount,String fromAccountNumber, String toAccountNumber){
        Optional<Account> oFromAcct = accountRepo.findById(fromAccountNumber);
        Optional<Account> oToAcct = accountRepo.findById(toAccountNumber);
        
        
        
        Transaction tranx = oFromAcct.map(fromAcct->{ 
           return oToAcct.map(toAcct->{ 
               accountRepo.updateBalance(fromAcct.getAccountnumber(),amount.negate());
               accountRepo.updateBalance(toAcct.getAccountnumber(),amount);
               return new Transaction(UUID.randomUUID().toString(),
                    amount,
                    new Date(),
                    fromAcct,
                    toAcct);
           }).orElseThrow(()->new TransactionException());
        }).orElseThrow(()->new TransactionException());
        trxRepo.save(tranx);
        accountRepo.flush();
        oFromAcct = accountRepo.findById(tranx.getFromaccount().getAccountnumber());
        return oFromAcct;
    }
    
    public Page<Transaction> getLatestTransactions(String accountNumber){
        return trxRepo.getLatest(accountNumber,PageRequest.of(0,10));
    }
    
    
}
