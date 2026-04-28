package com.msclientes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Persona {
    protected String nombre;
    protected String genero;
    protected int edad;
    protected String identificacion;
    protected String direccion;
    protected String telefono;

    public void actualizarDireccion(String direccion) {
        this.direccion = direccion;
    }
    public void actualizarTelefono(String telefono) {
        this.telefono = telefono;
    }
}
