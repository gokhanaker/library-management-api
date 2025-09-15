package com.applab.library_management.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        try {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Successfully sent message to topic '{}': {}", topic, message);
                } else {
                    logger.error("Failed to send message to topic '{}': {}", topic, message, ex);
                }
            });
            
            // Wait for the send operation to complete
            future.get();
            
        } catch (Exception e) {
            logger.error("Error sending message to topic '{}': {}", topic, message, e);
            throw new RuntimeException("Failed to send Kafka message", e);
        }
    }
}
