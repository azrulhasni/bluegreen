/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.ebanking.transfer.repository;

import com.azrul.ebanking.transfer.domain.Account;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author azrul
 */
@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, String>, JpaSpecificationExecutor<Account> {
   

    @Modifying
    @Query("update Account a set balance = balance + :transferedAmount where  a.accountnumber= :accountNumber")
    void updateBalance(String accountNumber, BigDecimal transferedAmount);
}

