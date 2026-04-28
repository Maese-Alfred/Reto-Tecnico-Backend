package com.mscuentas.infrastructure.persistence.repository;

import com.mscuentas.infrastructure.persistence.entity.MovimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaMovimientoRepository extends JpaRepository<MovimientoEntity,String> {

    List<MovimientoEntity> findByCuentaId(String cuentaId);

    List<MovimientoEntity> findByCuentaIdAndFechaBetween(String cuentaId, LocalDateTime inicio, LocalDateTime fin);
}
