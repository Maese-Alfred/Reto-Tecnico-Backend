package com.msclientes.application.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarClienteRequestDTO {
    private String clienteId;
    private String direccion;
    private String telefono;
}
