package com.picpay.picpaysimplified.service;

import com.picpay.picpaysimplified.domain.user.User;
import com.picpay.picpaysimplified.domain.user.UserType;
import com.picpay.picpaysimplified.dto.UserDTO;
import com.picpay.picpaysimplified.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;


    public boolean transactionValidator(User sender, BigDecimal amount) throws Exception {
        boolean isValidTransaction = false;
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new IllegalArgumentException("Merchant user can't send money");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Insufficient balance");
        }

        isValidTransaction = true;
        return isValidTransaction;
    }

    public User findUserById(Long id) {
        return this.userRepository.findUserById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserDTO userDTO) {
        User user = new User(userDTO);
        this.saveUser(user);
        return user;
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
        log.info("User saved successfully");
    }

    public User updateUser(Long id, BigDecimal newBalance) {
        User userById = userRepository.findById(id).get();
        userById.setBalance(newBalance);
        return userById;
    }
}
