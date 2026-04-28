package com.mscuentas.infrastructure.persistence.mapper;

import com.mscuentas.domain.model.Cuenta;
import com.mscuentas.domain.model.TipoCuenta;
import com.mscuentas.infrastructure.persistence.entity.CuentaEntity;

public class CuentaMapper {

    public static CuentaEntity toEntity(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity();
        entity.setCuentaId(cuenta.getCuentaId());
        entity.setNumeroCuenta(cuenta.getNumeroCuenta());
        entity.setTipoCuenta(cuenta.getTipoCuenta().name());
        entity.setSaldo(cuenta.getSaldo());
        entity.setEstado(cuenta.isEstado());
        entity.setClienteId(cuenta.getClienteId());
        return entity;
    }
    public static Cuenta toDomain(CuentaEntity entity) {
        return Cuenta.reconstituir(
                entity.getCuentaId(),
                entity.getNumeroCuenta(),
                TipoCuenta.valueOf(entity.getTipoCuenta()),
                entity.getSaldo(),
                entity.isEstado(),
                entity.getClienteId()
        );
    }

}
