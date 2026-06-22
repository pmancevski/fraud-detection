package com.paymentiq.frauddetection.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FraudAlert {
    private String alertId;
    private String transactionId;
    private String userId;
    private String ruleName;
    private String description;
    private String severity;  // LOW, MEDIUM, HIGH
    private LocalDateTime timestamp;
    private String status;    // NEW, REVIEWED, RESOLVED
}
