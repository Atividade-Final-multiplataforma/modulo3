package com.projetofinal.nair.lambda_consumer.application.service;

import com.projetofinal.nair.lambda_consumer.domain.model.EventMessage;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class EventProcessingService {

    public void process(String rawMessage) {
        EventMessage event = EventMessage.builder()
                .message(rawMessage)
                .source("kafka-cli")
                .type(detectType(rawMessage))
                .timestamp(Instant.now())
                .build();

        logToConsole(event);
    }

    private String detectType(String rawMessage) {
        String trimmed = rawMessage == null ? "" : rawMessage.trim();
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            return "JSON_GUEST";
        }
        return "PLAIN_TEXT";
    }

    private void logToConsole(EventMessage event) {
        System.out.println("A mensagem chegou: " + event.getMessage());

        System.out.printf(
                "[LAMBDA-NAIR] msg=\"%s\" source=%s type=%s ts=%s%n",
                event.getMessage(),
                event.getSource(),
                event.getType(),
                event.getTimestamp()
        );
    }
}
