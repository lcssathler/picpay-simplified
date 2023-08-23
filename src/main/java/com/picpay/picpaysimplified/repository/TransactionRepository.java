package com.picpay.picpaysimplified.repository;

import com.picpay.picpaysimplified.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
