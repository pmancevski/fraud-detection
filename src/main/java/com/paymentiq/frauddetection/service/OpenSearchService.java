package com.paymentiq.frauddetection.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paymentiq.frauddetection.model.FraudAlert;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenSearchService {
    private static final Logger log = LoggerFactory.getLogger(OpenSearchService.class);

    private final RestHighLevelClient openSearchClient;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    public OpenSearchService(RestHighLevelClient openSearchClient) {
        this.openSearchClient = openSearchClient;
    }

    public void indexAlert(FraudAlert alert) {
        try {
            String json = objectMapper.writeValueAsString(alert);
            IndexRequest request = new IndexRequest("fraud-alerts")
                    .id(alert.getAlertId())
                    .source(json, XContentType.JSON);

            IndexResponse response = openSearchClient.index(request, RequestOptions.DEFAULT);
            log.info("Alert indexed in OpenSearch: {}", response.getId());
        } catch (Exception e) {
            log.error("Failed to index alert in OpenSearch: {}", e.getMessage());
        }
    }
}
