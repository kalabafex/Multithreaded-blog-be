package com.MTBBE.MTB.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "blog-events";
    private static final String BLOG_ERRORS_TOPIC = "blog-errors";

    public void sendMessage(String key, String message) {
        kafkaTemplate.send(TOPIC, key, message);
    }

    public void sendErrorMessage(String key, String errorMessage) {
        kafkaTemplate.send(BLOG_ERRORS_TOPIC, key, errorMessage);
    }
}

