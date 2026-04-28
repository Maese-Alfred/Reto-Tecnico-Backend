package com.mscuentas.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class MovimientoReporteDTO {

    private LocalDateTime fecha;
    private String tipo;
    private BigDecimal valor;
    private BigDecimal saldo;
}
