package com.mscuentas.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class CuentaResponseDTO {
    private String cuentaId;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldo;
    private boolean estado;
    private String clienteId;
}
