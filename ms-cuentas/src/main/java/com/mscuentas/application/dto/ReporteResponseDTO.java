package com.mscuentas.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReporteResponseDTO {
    private String clienteId;
    private List<CuentaReporteDTO> cuentas;
}
