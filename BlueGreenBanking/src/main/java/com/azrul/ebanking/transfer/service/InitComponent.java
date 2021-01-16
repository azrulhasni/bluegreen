/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.ebanking.transfer.service;

import com.azrul.ebanking.transfer.domain.Account;
import com.azrul.ebanking.transfer.repository.AccountRepository;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author azrul
 */
@Component
public class InitComponent {
     @Autowired
    private AccountRepository accountRepo;
    
     @PostConstruct
    public void init(){
        String pattern = "00000000";
        DecimalFormat df = new DecimalFormat(pattern);
        for (int i=0;i<2500000;i++){
            Account account = new Account();
            account.setAccountnumber("A"+df.format(i));
            account.setBalance(new BigDecimal("5999999.99"));
            account.setFriendlyname("My day-to-day account");
            account.setProduct("Saving account");
            accountRepo.save(account);
        }
    }
}
