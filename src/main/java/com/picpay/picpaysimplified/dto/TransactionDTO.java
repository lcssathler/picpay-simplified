package com.picpay.picpaysimplified.dto;

import com.picpay.picpaysimplified.domain.user.User;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal value, Long senderId, Long receiverId) {
}
