package com.msclientes.application.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRequestDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String genero;

    @Min(0) @Max(120)
    private int edad;

    @NotBlank
    private String identificacion;

    @NotBlank
    private String direccion;

    @NotBlank
    private String telefono;

    @NotBlank
    private String contrasena;
}
