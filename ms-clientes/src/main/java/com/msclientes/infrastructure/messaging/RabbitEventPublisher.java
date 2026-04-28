package com.msclientes.infrastructure.messaging;

import com.msclientes.domain.event.EventPublisher;
import com.msclientes.infrastructure.messaging.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitEventPublisher implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(Object event) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event);
    }
}
