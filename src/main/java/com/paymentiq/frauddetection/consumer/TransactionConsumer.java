package com.paymentiq.frauddetection.consumer;

import com.paymentiq.frauddetection.model.Transaction;
import com.paymentiq.frauddetection.service.FraudDetectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransactionConsumer.class);

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @KafkaListener(topics = "${spring.kafka.consumer.topic-trEvents}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(@Payload Transaction transaction) {

        log.info("Received transaction: {}", transaction.getTransactionId());
        fraudDetectionService.processTransaction(transaction);
    }
}
