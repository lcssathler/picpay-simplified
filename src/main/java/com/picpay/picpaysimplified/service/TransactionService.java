package com.picpay.picpaysimplified.service;

import com.picpay.picpaysimplified.domain.transaction.Transaction;
import com.picpay.picpaysimplified.domain.user.User;
import com.picpay.picpaysimplified.dto.TransactionDTO;
import com.picpay.picpaysimplified.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

public class TransactionService {
    @Autowired
    UserService userService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    RestTemplate restTemplate;

    public void createTransaction(TransactionDTO transactionDTO) throws Exception {
        User sender = userService.findUserById(transactionDTO.senderId());
        User receiver = userService.findUserById(transactionDTO.receiverId());

        if (!userService.transactionValidator(sender, transactionDTO.value())) {
            throw new RuntimeException("Error completing transaction");
        }

        boolean isAuthorized = authorizedTransaction();
        if (!isAuthorized) throw new RuntimeException("Transaction denied");

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.value());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        this.transactionRepository.save(transaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        sender.getBalance().subtract(transaction.getAmount());
        receiver.getBalance().add(transaction.getAmount());
    }


    public boolean authorizedTransaction() {
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);
        if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
            String message = (String) authorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        } else return false;
    }
}
