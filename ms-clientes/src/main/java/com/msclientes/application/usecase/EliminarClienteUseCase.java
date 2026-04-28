package com.msclientes.application.usecase;

import com.msclientes.domain.event.ClienteEliminadoEvent;
import com.msclientes.domain.event.EventPublisher;
import com.msclientes.domain.exception.DomainException;
import com.msclientes.domain.model.Cliente;
import com.msclientes.domain.repository.ClienteRepository;

import java.time.LocalDateTime;

public class EliminarClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final EventPublisher eventPublisher;

    public EliminarClienteUseCase(ClienteRepository clienteRepository, EventPublisher eventPublisher) {
        this.clienteRepository = clienteRepository;
        this.eventPublisher = eventPublisher;
    }

    public void execute(String clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new DomainException("Cliente no encontrado: " + clienteId));

        cliente.desactivar();
        clienteRepository.save(cliente);

        eventPublisher.publish(new ClienteEliminadoEvent(clienteId, LocalDateTime.now()));
    }
}
