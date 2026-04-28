package com.mscuentas.infrastructure.persistence.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name= "movimientos")
public class MovimientoEntity {

    @Id
    @Column(name = "movimiento_id")
    private String movimientoId;

    private LocalDateTime fecha;
    private String tipoMovimiento;
    private BigDecimal saldo;
    private BigDecimal valor;
    private String cuentaId;
}
