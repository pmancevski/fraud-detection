package com.paymentiq.frauddetection.service;

import com.paymentiq.frauddetection.model.FraudAlert;
import com.paymentiq.frauddetection.model.Transaction;
import com.paymentiq.frauddetection.producer.AlertProducer;
import com.paymentiq.frauddetection.rule.Rule;
import com.paymentiq.frauddetection.rule.RuleEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FraudDetectionService {
    private static final Logger log = LoggerFactory.getLogger(FraudDetectionService.class);

    private final RuleEngine ruleEngine;
    private final AlertProducer alertProducer;

    private final SnowTransactionService snowTransactionService;
    private final SnowAlertService snowAlertService;

    private final OpenSearchService openSearchService;

    public FraudDetectionService(RuleEngine ruleEngine, AlertProducer alertProducer,
                                 SnowTransactionService snowTransactionService, SnowAlertService snowAlertService,
                                 OpenSearchService openSearchService) {
        this.ruleEngine = ruleEngine;
        this.alertProducer = alertProducer;
        this.snowTransactionService = snowTransactionService;
        this.snowAlertService = snowAlertService;
        this.openSearchService = openSearchService;
    }

    public void processTransaction(Transaction transaction) {
        log.info("Processing transaction: {}", transaction.getTransactionId());

        // save to snowflake transaction table
        snowTransactionService.saveTransaction(transaction);

        List<Rule> triggeredRules = ruleEngine.evaluate(transaction);

        if (!triggeredRules.isEmpty()) {

            FraudAlert alert = createAlert(transaction, triggeredRules);
            alertProducer.sendAlert(alert);

            // save to snowflake fraud table
            snowAlertService.saveAlert(alert);

            openSearchService.indexAlert(alert);
            log.warn("Fraud alert sent for transaction: {}", transaction.getTransactionId());

        } else {
            log.info("Transaction approved: {}", transaction.getTransactionId());
        }
    }

    private FraudAlert createAlert(Transaction transaction, List<Rule> triggeredRules) {

        FraudAlert alert = new FraudAlert();
        alert.setAlertId(UUID.randomUUID().toString());
        alert.setTransactionId(transaction.getTransactionId());
        alert.setUserId(transaction.getUserId());
        alert.setRuleName(triggeredRules.get(0).getRuleName());
        alert.setDescription(triggeredRules.get(0).getDescription());
        alert.setSeverity(triggeredRules.get(0).getSeverity());
        alert.setTimestamp(LocalDateTime.now());
        alert.setStatus("NEW");

        return alert;
    }
}
