package com.msclientes.infrastructure.persistence.adapter;

import com.msclientes.domain.model.Cliente;
import com.msclientes.domain.repository.ClienteRepository;
import com.msclientes.infrastructure.persistence.entity.ClienteEntity;
import com.msclientes.infrastructure.persistence.mapper.ClienteMapper;
import com.msclientes.infrastructure.persistence.repository.JpaClienteRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClienteRepositoryAdapter implements ClienteRepository {

    private final JpaClienteRepository jpaRepository;

    public ClienteRepositoryAdapter(JpaClienteRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        ClienteEntity entity = ClienteMapper.toEntity(cliente);
        return ClienteMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Cliente> findById(String id) {
        return jpaRepository.findById(id)
                .map(ClienteMapper::toDomain);
    }

    @Override
    public Optional<Cliente> findByIdentificacion(String identificacion) {
        return jpaRepository.findByIdentificacion(identificacion)
                .map(ClienteMapper::toDomain);
    }

    @Override
    public List<Cliente> findAll() {
        return jpaRepository.findAll().stream()
                .map(ClienteMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Cliente cliente) {
        ClienteEntity entity = ClienteMapper.toEntity(cliente);
        jpaRepository.delete(entity);
    }
}
