package com.paymentiq.frauddetection.service;

import com.paymentiq.frauddetection.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class SnowTransactionService {

    private static final Logger log = LoggerFactory.getLogger(SnowTransactionService.class);

    private final Connection snowflakeConnection;

    @Autowired
    public SnowTransactionService(Connection snowflakeConnection) {
        this.snowflakeConnection = snowflakeConnection;
    }

    public void saveTransaction(Transaction transaction) {
        String sql = """
            INSERT INTO TRANSACTIONS (TRANSACTION_ID, USER_ID, AMOUNT, CURRENCY, LOCATION, DEVICE_ID, TIMESTAMP, MERCHANT)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = snowflakeConnection.prepareStatement(sql)) {
            stmt.setString(1, transaction.getTransactionId());
            stmt.setString(2, transaction.getUserId());
            stmt.setBigDecimal(3, transaction.getAmount());
            stmt.setString(4, transaction.getCurrency());
            stmt.setString(5, transaction.getLocation());
            stmt.setString(6, transaction.getDeviceId());
            stmt.setTimestamp(7, java.sql.Timestamp.valueOf(transaction.getTimestamp()));
            stmt.setString(8, transaction.getMerchant());
            stmt.executeUpdate();
            log.info("Transaction saved: {}", transaction.getTransactionId());
        }
        catch (SQLException e) {
            log.error("Failed to save transaction: {}", e.getMessage());
        }
    }
}
