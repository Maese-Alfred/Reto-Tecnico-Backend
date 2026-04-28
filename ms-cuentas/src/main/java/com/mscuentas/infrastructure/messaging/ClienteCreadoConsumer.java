package com.mscuentas.infrastructure.messaging;

import com.mscuentas.domain.event.ClienteCreadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ClienteCreadoConsumer {

    private static final Logger log = LoggerFactory.getLogger(ClienteCreadoConsumer.class);

    @RabbitListener(queues = "cliente.creado.queue")
    public void consumir(ClienteCreadoEvent event) {
        log.info("Evento recibido: clienteId={} identificacion={} fecha={}",
                event.getClienteId(), event.getIdentificacion(), event.getFecha());
    }
}