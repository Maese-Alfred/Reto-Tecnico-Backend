package com.mscuentas.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Movimiento {

    private String movimientoId;
    private LocalDateTime fecha;
    private TipoMovimiento tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private String cuentaId;

    public Movimiento(TipoMovimiento tipoMovimiento, BigDecimal valor, BigDecimal saldo, String cuentaId) {
        this.movimientoId = UUID.randomUUID().toString();
        this.fecha = LocalDateTime.now();
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.cuentaId = cuentaId;
    }

    public static Movimiento reconstituir(String movimientoId, LocalDateTime fecha,
            TipoMovimiento tipoMovimiento, BigDecimal valor, BigDecimal saldo, String cuentaId) {
        Movimiento m = new Movimiento(tipoMovimiento, valor, saldo, cuentaId);
        m.movimientoId = movimientoId;
        m.fecha = fecha;
        return m;
    }
}
