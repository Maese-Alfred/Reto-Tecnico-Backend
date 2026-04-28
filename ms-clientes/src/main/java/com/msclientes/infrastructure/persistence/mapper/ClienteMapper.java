package com.msclientes.infrastructure.persistence.mapper;

import com.msclientes.domain.model.Cliente;
import com.msclientes.infrastructure.persistence.entity.ClienteEntity;

public class ClienteMapper {

    public static ClienteEntity toEntity(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();
        entity.setClienteId(cliente.getClienteId());
        entity.setNombre(cliente.getNombre());
        entity.setGenero(cliente.getGenero());
        entity.setEdad(cliente.getEdad());
        entity.setIdentificacion(cliente.getIdentificacion());
        entity.setTelefono(cliente.getTelefono());
        entity.setDireccion(cliente.getDireccion());
        entity.setContrasena(cliente.getContrasena());
        entity.setEstado(cliente.isEstado());
        return entity;
    }
    public static Cliente toDomain(ClienteEntity entity) {
        return Cliente.reconstituir(
                entity.getClienteId(),
                entity.getNombre(),
                entity.getGenero(),
                entity.getEdad(),
                entity.getIdentificacion(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getContrasena(),
                entity.isEstado()
        );
    }
}
