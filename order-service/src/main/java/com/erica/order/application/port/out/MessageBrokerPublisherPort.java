package com.erica.order.application.port.out;

public interface MessageBrokerPublisherPort {
    void publicar(String topicoOuEvento, String chave, String payload);
}
