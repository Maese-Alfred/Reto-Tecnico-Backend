package com.msclientes.application.usecase;

import com.msclientes.application.dto.ClienteResponseDTO;
import com.msclientes.application.dto.ClienteRequestDTO;
import com.msclientes.domain.event.ClienteCreadoEvent;
import com.msclientes.domain.event.EventPublisher;
import com.msclientes.domain.model.Cliente;
import com.msclientes.domain.repository.ClienteRepository;
import com.msclientes.domain.service.ClienteDomainService;

import java.time.LocalDateTime;

public class CrearClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final ClienteDomainService clienteDomainService;
    private final EventPublisher eventPublisher;

    public CrearClienteUseCase(ClienteRepository clienteRepository,
                               ClienteDomainService clienteDomainService,
                               EventPublisher eventPublisher) {
        this.clienteRepository = clienteRepository;
        this.clienteDomainService = clienteDomainService;
        this.eventPublisher = eventPublisher;
    }

    public ClienteResponseDTO execute(ClienteRequestDTO request) {

        Cliente cliente = new Cliente(
                request.getNombre(),
                request.getGenero(),
                request.getEdad(),
                request.getIdentificacion(),
                request.getDireccion(),
                request.getTelefono(),
                request.getContrasena()
        );

        clienteDomainService.crearCliente(cliente);

        Cliente saved = clienteRepository.save(cliente);

        ClienteCreadoEvent event = new ClienteCreadoEvent(
                saved.getClienteId(),
                saved.getIdentificacion(),
                LocalDateTime.now()
        );

        eventPublisher.publish(event);

        return new ClienteResponseDTO(
                saved.getClienteId(),
                saved.getNombre(),
                saved.isEstado()
        );
    }
}