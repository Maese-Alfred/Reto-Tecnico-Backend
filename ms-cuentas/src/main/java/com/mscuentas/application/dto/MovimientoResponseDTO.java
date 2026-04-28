package com.mscuentas.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class MovimientoResponseDTO {
    private String movimientoId;
    private BigDecimal saldoDisponible;
}
