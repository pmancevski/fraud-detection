package com.paymentiq.frauddetection.rule;

import com.paymentiq.frauddetection.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocationRule implements Rule {

    private static final Logger log = LoggerFactory.getLogger(LocationRule.class);
    private final Map<String, String> lastLocation = new ConcurrentHashMap<>();

    @Override
    public boolean evaluate(Transaction transaction) {
        String userId = transaction.getUserId();
        String location = transaction.getLocation();

        if (location == null || location.isEmpty()) {
            return false;
        }

        String prevLocation = lastLocation.get(userId);

        if (prevLocation == null) {
            lastLocation.put(userId, location);
            return false;
        }

        boolean locationChanged = !prevLocation.equals(location);

        if (locationChanged) {
            lastLocation.put(userId, location);
            log.warn("Location rule triggered for user: {} from {} to {}", userId, prevLocation, location);
        }

        return locationChanged;
    }

    @Override
    public String getRuleName() {
        return "Location Rule";
    }

    @Override
    public String getDescription() {
        return "Transaction from a different location than previous";
    }

    @Override
    public String getSeverity() {
        return "MEDIUM";
    }
}
