package com.paymentiq.frauddetection.service;

import com.paymentiq.frauddetection.model.FraudAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class SnowAlertService {

    private static final Logger log = LoggerFactory.getLogger(SnowAlertService.class);

    private final Connection snowflakeConnection;

    @Autowired
    public SnowAlertService(Connection snowflakeConnection) {
        this.snowflakeConnection = snowflakeConnection;
    }

    public void saveAlert(FraudAlert alert) {
        String sql = """
            INSERT INTO FRAUD_ALERTS (ALERT_ID, TRANSACTION_ID, USER_ID, RULE_NAME, DESCRIPTION, SEVERITY, TIMESTAMP, STATUS)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = snowflakeConnection.prepareStatement(sql)) {
            stmt.setString(1, alert.getAlertId());
            stmt.setString(2, alert.getTransactionId());
            stmt.setString(3, alert.getUserId());
            stmt.setString(4, alert.getRuleName());
            stmt.setString(5, alert.getDescription());
            stmt.setString(6, alert.getSeverity());
            stmt.setTimestamp(7, java.sql.Timestamp.valueOf(alert.getTimestamp()));
            stmt.setString(8, alert.getStatus());
            stmt.executeUpdate();
            log.info("Alert saved: {}", alert.getAlertId());
        }
        catch (SQLException e) {
            log.error("Failed to save alert: {}", e.getMessage());
        }
    }
}
