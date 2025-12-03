package com.projetofinal.nair.lambda_consumer.api.listener;

import com.projetofinal.nair.lambda_consumer.application.service.EventProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventListener {

    private final EventProcessingService eventProcessingService;

    @KafkaListener(
            topics = "${app.kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onMessage(String payload) {
        eventProcessingService.process(payload);
    }
}
