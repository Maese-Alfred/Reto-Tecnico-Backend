package com.msclientes.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clientes")
public class ClienteEntity {
    @Id
    @Column(name = "cliente_id")
    private String clienteId;

    private String nombre;
    private String genero;
    private int edad;

    @Column(unique = true)
    private String identificacion;

    private String direccion;
    private String telefono;

    private String contrasena;
    private boolean estado;
}
