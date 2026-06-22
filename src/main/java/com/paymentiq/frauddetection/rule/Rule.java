package com.paymentiq.frauddetection.rule;

import com.paymentiq.frauddetection.model.Transaction;

public interface Rule {

    boolean evaluate(Transaction transaction);

    String getRuleName();
    String getDescription();
    String getSeverity();  // LOW, MEDIUM, HIGH
}
