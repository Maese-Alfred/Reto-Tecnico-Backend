package com.mscuentas.domain.strategy;

import com.mscuentas.domain.exception.SaldoNoDisponibleException;
import com.mscuentas.domain.model.Cuenta;

import java.math.BigDecimal;

public class RetiroStrategy implements MovimientoStrategy {

    @Override
    public void aplicar(Cuenta cuenta, BigDecimal valor) {
        if(cuenta.getSaldo().compareTo(valor) < 0){
            throw new SaldoNoDisponibleException("Saldo no disponible");

        }
        cuenta.retirar(valor);
    }
}
