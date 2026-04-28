package com.mscuentas.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class CuentaReporteDTO {

    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldo;
    private List<MovimientoReporteDTO> movimientos;
}
