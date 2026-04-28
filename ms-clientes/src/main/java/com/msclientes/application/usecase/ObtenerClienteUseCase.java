package com.msclientes.application.usecase;

import com.msclientes.application.dto.ClienteDetalleResponseDTO;
import com.msclientes.domain.exception.DomainException;
import com.msclientes.domain.model.Cliente;
import com.msclientes.domain.repository.ClienteRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ObtenerClienteUseCase {

    private final ClienteRepository clienteRepository;

    public ObtenerClienteUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteDetalleResponseDTO findById(String clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new DomainException("Cliente no encontrado: " + clienteId));
        return toDTO(cliente);
    }

    public List<ClienteDetalleResponseDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ClienteDetalleResponseDTO toDTO(Cliente c) {
        return new ClienteDetalleResponseDTO(
                c.getClienteId(),
                c.getNombre(),
                c.getGenero(),
                c.getEdad(),
                c.getIdentificacion(),
                c.getDireccion(),
                c.getTelefono(),
                c.isEstado()
        );
    }
}
