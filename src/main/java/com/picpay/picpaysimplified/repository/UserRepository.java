package com.picpay.picpaysimplified.repository;

import com.picpay.picpaysimplified.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
