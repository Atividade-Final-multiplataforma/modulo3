package com.projetofinal.nair.lambda_consumer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventMessage {

    private String message;
    private String source;
    private String type;
    private Instant timestamp;
}
