package com.erica.order.infrastructure.adapter.out.messaging;

import com.erica.order.application.port.out.MessageBrokerPublisherPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class KafkaBrokerPublisherAdapter implements MessageBrokerPublisherPort {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaBrokerPublisherAdapter(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = Objects.requireNonNull(kafkaTemplate, "KafkaTemplate é obrigatório");
    }

    @Override
    public void publicar(String topicoOuEvento, String chave, String payload) {
        kafkaTemplate.send(topicoOuEvento, chave, payload);
    }
}
