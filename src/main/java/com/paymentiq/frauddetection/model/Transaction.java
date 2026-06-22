package com.paymentiq.frauddetection.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Transaction {
    private String transactionId;
    private String userId;
    private BigDecimal amount;
    private String currency;
    private String location;
    private String deviceId;
    private LocalDateTime timestamp;
    private String merchant;
}
