package com.mscuentas.infrastructure.persistence.adapter;

import com.mscuentas.domain.model.Movimiento;
import com.mscuentas.domain.repository.MovimientoRepository;
import com.mscuentas.infrastructure.persistence.mapper.MovimientoMapper;
import com.mscuentas.infrastructure.persistence.repository.JpaMovimientoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovimientoRepositoryAdapter implements MovimientoRepository {

    private final JpaMovimientoRepository jpaRepository;

    public MovimientoRepositoryAdapter(JpaMovimientoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Movimiento save(Movimiento movimiento) {
        return MovimientoMapper.toDomain(jpaRepository.save(MovimientoMapper.toEntity(movimiento)));
    }

    @Override
    public Optional<Movimiento> findById(String movimientoId) {
        return jpaRepository.findById(movimientoId).map(MovimientoMapper::toDomain);
    }

    @Override
    public List<Movimiento> findByCuentaId(String cuentaId) {
        return jpaRepository.findByCuentaId(cuentaId).stream()
                .map(MovimientoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movimiento> findByCuentaIdAndFechaBetween(String cuentaId, LocalDateTime inicio, LocalDateTime fin) {
        return jpaRepository.findByCuentaIdAndFechaBetween(cuentaId, inicio, fin)
                .stream()
                .map(MovimientoMapper::toDomain)
                .collect(Collectors.toList());
    }
}