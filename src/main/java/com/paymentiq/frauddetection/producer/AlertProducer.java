package com.paymentiq.frauddetection.producer;

import com.paymentiq.frauddetection.model.FraudAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AlertProducer {

    private static final Logger log = LoggerFactory.getLogger(AlertProducer.class);

    private final KafkaTemplate<String, FraudAlert> kafkaTemplate;

    @Value("${spring.kafka.producer.topic}")
    private String alertTopic;

    public AlertProducer(KafkaTemplate<String, FraudAlert> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAlert(FraudAlert alert) {
        kafkaTemplate.send(alertTopic, alert);
        log.info("Alert sent: {}", alert.getAlertId());
    }
}
