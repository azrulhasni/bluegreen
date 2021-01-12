/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.ebanking.transfer.repository;

import com.azrul.ebanking.transfer.domain.Account;
import com.azrul.ebanking.transfer.domain.Transaction;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author azrul
 */
@Repository
@Transactional
public interface TransactionRepository  extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    @Query(value = "SELECT t FROM Transaction t WHERE t.fromaccount.accountnumber=:fromAccountNumber ORDER BY t.datetime DESC")
    Page<Transaction> getLatest(String fromAccountNumber, Pageable pageable);
}
