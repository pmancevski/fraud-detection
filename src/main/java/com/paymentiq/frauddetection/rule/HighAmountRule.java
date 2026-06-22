package com.paymentiq.frauddetection.rule;

import com.paymentiq.frauddetection.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HighAmountRule implements Rule {
    private static final Logger log = LoggerFactory.getLogger(HighAmountRule.class);

    private static final double THRESHOLD = 10000.0;

    @Override
    public boolean evaluate(Transaction transaction) {
        log.debug("Evaluating high amount rule");

        return transaction.getAmount().doubleValue() > THRESHOLD;
    }

    @Override
    public String getRuleName() {
        return "High Amount Rule";
    }

    @Override
    public String getDescription() {
        return "Transaction amount exceeds " + THRESHOLD;
    }

    @Override
    public String getSeverity() {
        return "HIGH";
    }
}
