package com.paymentiq.frauddetection.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class SnowflakeConfig {

    private static final Logger log = LoggerFactory.getLogger(SnowflakeConfig.class);

    @Value("${snowflake.account}")
    private String account;

    @Value("${snowflake.user}")
    private String user;

    @Value("${snowflake.password}")
    private String password;

    @Value("${snowflake.warehouse}")
    private String warehouse;

    @Value("${snowflake.database}")
    private String database;

    @Value("${snowflake.schema}")
    private String schema;

    @Bean
    public Connection snowflakeConnection() throws SQLException {
        String url = String.format(
                "jdbc:snowflake://%s.snowflakecomputing.com/?user=%s&password=%s&warehouse=%s&db=%s&schema=%s",
                account, user, password, warehouse, database, schema
        );

        log.info("Snowflake connection established successfully!");

        return DriverManager.getConnection(url);
    }
}
