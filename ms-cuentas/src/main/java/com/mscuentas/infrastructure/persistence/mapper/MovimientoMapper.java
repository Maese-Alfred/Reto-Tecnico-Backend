package com.mscuentas.infrastructure.persistence.mapper;

import com.mscuentas.domain.model.Movimiento;
import com.mscuentas.domain.model.TipoMovimiento;
import com.mscuentas.infrastructure.persistence.entity.MovimientoEntity;

public class MovimientoMapper {

    public static MovimientoEntity toEntity(Movimiento movimiento) {
        MovimientoEntity entity = new MovimientoEntity();
        entity.setMovimientoId(movimiento.getMovimientoId());
        entity.setFecha(movimiento.getFecha());
        entity.setTipoMovimiento(movimiento.getTipoMovimiento().name());
        entity.setValor(movimiento.getValor());
        entity.setSaldo(movimiento.getSaldo());
        entity.setCuentaId(movimiento.getCuentaId());
        return entity;
    }

    public static Movimiento toDomain(MovimientoEntity entity) {
        return Movimiento.reconstituir(
                entity.getMovimientoId(),
                entity.getFecha(),
                TipoMovimiento.valueOf(entity.getTipoMovimiento()),
                entity.getValor(),
                entity.getSaldo(),
                entity.getCuentaId()
        );
    }
}