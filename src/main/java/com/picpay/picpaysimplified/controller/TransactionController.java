package com.picpay.picpaysimplified.controller;

import com.picpay.picpaysimplified.domain.transaction.Transaction;
import com.picpay.picpaysimplified.dto.TransactionDTO;
import com.picpay.picpaysimplified.service.TransactionService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionDTO) throws Exception {
        Transaction newTransaction = transactionService.createTransaction(transactionDTO);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactionsList = transactionService.findAllTransactions();
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }
}
