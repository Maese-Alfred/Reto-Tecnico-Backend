package com.msclientes.application.usecase;

import com.msclientes.application.dto.ActualizarClienteRequestDTO;
import com.msclientes.application.dto.ClienteResponseDTO;
import com.msclientes.domain.exception.DomainException;
import com.msclientes.domain.model.Cliente;
import com.msclientes.domain.repository.ClienteRepository;


public class ActualizarClienteUseCase {

    private final ClienteRepository clienteRepository;

    public ActualizarClienteUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponseDTO execute(ActualizarClienteRequestDTO requestDTO) {
        Cliente cliente = clienteRepository.findById(requestDTO.getClienteId())
                .orElseThrow(() -> new DomainException("Cliente no encontrado"));

        if(requestDTO.getDireccion() != null && !requestDTO.getDireccion().isEmpty()){
            cliente.actualizarDireccion(requestDTO.getDireccion());
        }
        if(requestDTO.getTelefono() != null && !requestDTO.getTelefono().isEmpty()){
            cliente.actualizarTelefono(requestDTO.getTelefono());
        }

        Cliente updated = clienteRepository.save(cliente);

        return new ClienteResponseDTO(
                updated.getClienteId(),
                updated.getNombre(),
                updated.isEstado()
        );
    }
}
