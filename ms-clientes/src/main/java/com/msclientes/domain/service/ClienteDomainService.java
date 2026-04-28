package com.msclientes.domain.service;

import com.msclientes.domain.exception.DomainException;
import com.msclientes.domain.model.Cliente;
import com.msclientes.domain.repository.ClienteRepository;

public class ClienteDomainService {

    private final ClienteRepository clienteRepository;

    public ClienteDomainService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void validarUnicidadIdentificacion(String identificacion){
        if(clienteRepository.findByIdentificacion(identificacion).isPresent()){
            throw new DomainException("El cliente existe en el sistema");
        }
    }

    public Cliente crearCliente(Cliente cliente){
        validarUnicidadIdentificacion(cliente.getIdentificacion());
        return cliente;
    }
}
