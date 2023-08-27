package com.picpay.picpaysimplified.controller;

import com.picpay.picpaysimplified.domain.user.User;
import com.picpay.picpaysimplified.dto.UserDTO;
import com.picpay.picpaysimplified.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController()
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        User newUser = userService.createUser(userDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.findAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, BigDecimal newBalance) {
        newBalance = BigDecimal.ONE;
        User user = userService.updateUser(id, newBalance);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
