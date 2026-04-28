package com.msclientes.domain.model;
import lombok.Getter;
import java.util.UUID;

@Getter
public class Cliente extends Persona {
    private String clienteId;
    private String contrasena;
    private boolean estado;

    public Cliente(String nombre, String genero, int edad,String identificacion, String direccion, String telefono,String contrasena) {
        super(nombre,genero,edad,identificacion,direccion,telefono);
        this.clienteId = UUID.randomUUID().toString();
        this.contrasena = contrasena;
        this.estado = true;
    }

    public static Cliente reconstituir(String clienteId, String nombre, String genero,
            int edad, String identificacion, String direccion, String telefono,
            String contrasena, boolean estado) {
        Cliente c = new Cliente(nombre, genero, edad, identificacion, direccion, telefono, contrasena);
        c.clienteId = clienteId;
        c.estado = estado;
        return c;
    }

    public void cambiarContrasena(String nueva) {
        this.contrasena = nueva;
    }

    public void desactivar(){
        this.estado = false;
    }

    public void activar(){
        this.estado = true;
    }
}
