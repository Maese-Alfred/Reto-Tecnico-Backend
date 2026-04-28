package com.msclientes.infrastructure.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE    = "clientes.exchange";
    public static final String QUEUE       = "cliente.creado.queue";
    public static final String ROUTING_KEY = "cliente.creado";

    @Bean
    public DirectExchange clientesExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue clienteQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public Binding clienteBinding(Queue clienteQueue, DirectExchange clientesExchange) {
        return BindingBuilder.bind(clienteQueue).to(clientesExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}