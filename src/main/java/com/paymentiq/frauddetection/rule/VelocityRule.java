package com.paymentiq.frauddetection.rule;

import com.paymentiq.frauddetection.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VelocityRule implements Rule {

    private static final Logger log = LoggerFactory.getLogger(VelocityRule.class);

    private final Map<String, LocalDateTime> lastTransactionTime = new ConcurrentHashMap<>();

    private static final int MINUTES_THRESHOLD = 1;
    private static final int MAX_TRANSACTIONS = 3;

    @Override
    public boolean evaluate(Transaction transaction) {
        String userId = transaction.getUserId();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime last = lastTransactionTime.get(userId);

        if (last == null) {
            lastTransactionTime.put(userId, now);
            return false;
        }

        boolean isFrequent = last.plusMinutes(MINUTES_THRESHOLD).isAfter(now);
        lastTransactionTime.put(userId, now);

        if (isFrequent) {
            log.warn("Velocity rule triggered for user: {}", userId);
        }
        return isFrequent;
    }

    @Override
    public String getRuleName() {
        return "Velocity Rule";
    }

    @Override
    public String getDescription() {
        return "More than " + MAX_TRANSACTIONS + " transactions in " + MINUTES_THRESHOLD + " minute(s)";
    }

    @Override
    public String getSeverity() {
        return "MEDIUM";
    }
}
