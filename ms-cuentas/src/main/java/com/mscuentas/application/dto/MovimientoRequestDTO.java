package com.mscuentas.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MovimientoRequestDTO {

    @NotBlank
    private String cuentaId;

    @NotBlank
    private String tipoMovimiento;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal valor;
}
