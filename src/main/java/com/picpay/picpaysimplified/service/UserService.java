package com.picpay.picpaysimplified.service;

import com.picpay.picpaysimplified.domain.user.User;
import com.picpay.picpaysimplified.domain.user.UserType;
import com.picpay.picpaysimplified.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Log4j2
public class UserService {
    @Autowired
    UserRepository userRepository;


    public void validaTransaction(User sender, BigDecimal amount) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new IllegalArgumentException("Merchant user can't send money");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Insufficient balance");
        }
    }

    public User findUserById(Long id) {
        return this.userRepository.findUserById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
        log.info("User saved successfully");

    }
}
