package com.mscuentas.domain.repository;

import com.mscuentas.domain.model.Movimiento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepository {

    Movimiento save(Movimiento movimiento);

    Optional<Movimiento> findById(String movimientoId);

    List<Movimiento> findByCuentaId(String cuentaId);

    List<Movimiento> findByCuentaIdAndFechaBetween(String cuentaId, LocalDateTime inicio, LocalDateTime fin);
}
