package com.mscuentas.domain.strategy;

import com.mscuentas.domain.model.Cuenta;

import java.math.BigDecimal;

public class DepositoStrategy implements MovimientoStrategy {
    @Override
    public void aplicar(Cuenta cuenta, BigDecimal valor) {
        cuenta.depositar(valor);
    }

}
