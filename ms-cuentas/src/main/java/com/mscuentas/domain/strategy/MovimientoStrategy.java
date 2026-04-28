package com.mscuentas.domain.strategy;

import com.mscuentas.domain.model.Cuenta;

import java.math.BigDecimal;

public interface MovimientoStrategy {

    void aplicar(Cuenta cuenta, BigDecimal valor);
}
