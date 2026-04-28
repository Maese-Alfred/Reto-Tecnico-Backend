package com.mscuentas.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "cuentas")
public class CuentaEntity {
    @Id
    @Column(name = "cuenta_id")
    private String cuentaId;

    @Column(unique = true)
    private String numeroCuenta;

    private String tipoCuenta;
    private BigDecimal saldo;
    private boolean estado;
    private String clienteId;
}
