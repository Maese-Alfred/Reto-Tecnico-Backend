package com.mscuentas.infrastructure.messaging.config;

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

    public static final String EXCHANGE    = "movimientos.exchange";
    public static final String ROUTING_KEY = "movimiento.registrado";

    /** Cola entrante: eventos de clientes publicados por ms-clientes */
    public static final String CLIENTE_CREADO_QUEUE = "cliente.creado.queue";

    @Bean
    public DirectExchange movimientosExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue clienteCreadoQueue() {
        return QueueBuilder.durable(CLIENTE_CREADO_QUEUE).build();
    }

    @Bean
    public Binding movimientoBinding(Queue clienteCreadoQueue, DirectExchange movimientosExchange) {
        return BindingBuilder.bind(clienteCreadoQueue).to(movimientosExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
