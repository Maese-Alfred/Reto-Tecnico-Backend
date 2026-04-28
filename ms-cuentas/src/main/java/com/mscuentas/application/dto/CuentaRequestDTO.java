package com.mscuentas.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CuentaRequestDTO {

    @NotBlank
    private String numeroCuenta;

    @NotBlank
    private String tipoCuenta;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal saldoInicial;

    @NotBlank
    private String clienteId;
}
