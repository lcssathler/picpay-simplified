package com.picpay.picpaysimplified.service;

import com.picpay.picpaysimplified.domain.transaction.Transaction;
import com.picpay.picpaysimplified.domain.user.User;
import com.picpay.picpaysimplified.dto.TransactionDTO;
import com.picpay.picpaysimplified.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    UserService userService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    NotificationService notificationService;



    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
        User sender = userService.findUserById(transactionDTO.senderId());
        User receiver = userService.findUserById(transactionDTO.receiverId());

        if (!userService.transactionValidator(sender, transactionDTO.value())) {
            throw new Exception("Error completing transaction");
        }

        boolean isAuthorized = authorizedTransaction();
        if (!isAuthorized) throw new Exception("Transaction denied");

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transactionDTO.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDTO.value()));
        receiver.setBalance(receiver.getBalance().add(transactionDTO.value()));

        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        notificationService.sendNotification(sender, "You sent a transaction");
        notificationService.sendNotification(receiver, "You received a transaction");

        return newTransaction;
    }

    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }


    public boolean authorizedTransaction() {
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);
        if (authorizationResponse.getStatusCode() != HttpStatus.OK) return false;

        String message = (String) authorizationResponse.getBody().get("message");
        return "Autorizado".equalsIgnoreCase(message);
    }
}
