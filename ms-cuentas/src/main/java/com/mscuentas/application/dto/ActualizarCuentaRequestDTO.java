package com.mscuentas.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarCuentaRequestDTO {
    private String tipoCuenta;
    private Boolean estado;
}
