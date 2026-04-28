package com.mscuentas.domain.service;

import com.mscuentas.domain.model.Cuenta;
import com.mscuentas.domain.model.Movimiento;
import com.mscuentas.domain.model.TipoMovimiento;
import com.mscuentas.domain.strategy.DepositoStrategy;
import com.mscuentas.domain.strategy.MovimientoStrategy;
import com.mscuentas.domain.strategy.RetiroStrategy;

import java.math.BigDecimal;

public class MovimientoDomainService {

    public Movimiento registrarMovimiento(Cuenta cuenta, TipoMovimiento tipo, BigDecimal valor){

        MovimientoStrategy strategy = obtenerStrategy(tipo);

        strategy.aplicar(cuenta,valor);

        return new Movimiento(
                tipo,
                valor,
                cuenta.getSaldo(),
                cuenta.getCuentaId()
        );
    }

    private MovimientoStrategy obtenerStrategy(TipoMovimiento tipo){
        return switch (tipo){
            case DEPOSITO -> new DepositoStrategy();
            case RETIRO ->  new RetiroStrategy();
        };
    }
}
