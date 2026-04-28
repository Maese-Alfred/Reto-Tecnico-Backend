package com.mscuentas.infrastructure.persistence.adapter;

import com.mscuentas.domain.model.Cuenta;
import com.mscuentas.domain.repository.CuentaRepository;
import com.mscuentas.infrastructure.persistence.mapper.CuentaMapper;
import com.mscuentas.infrastructure.persistence.repository.JpaCuentaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CuentaRepositoryAdapter implements CuentaRepository {

    private final JpaCuentaRepository jpaRepository;

    public CuentaRepositoryAdapter(JpaCuentaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cuenta save(Cuenta cuenta) {
        return CuentaMapper.toDomain(jpaRepository.save(CuentaMapper.toEntity(cuenta)));
    }

    @Override
    public Optional<Cuenta> findById(String cuentaId) {
        return jpaRepository.findById(cuentaId).map(CuentaMapper::toDomain);
    }

    @Override
    public List<Cuenta> findByClienteId(String clienteId) {
        return jpaRepository.findByClienteId(clienteId)
                .stream()
                .map(CuentaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cuenta> findAll() {
        return jpaRepository.findAll().stream()
                .map(CuentaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String cuentaId) {
        jpaRepository.deleteById(cuentaId);
    }
}
