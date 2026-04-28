package com.msclientes.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ClienteResponseDTO {

    private String clienteId;
    private String nombre;
    private boolean estado;
}
