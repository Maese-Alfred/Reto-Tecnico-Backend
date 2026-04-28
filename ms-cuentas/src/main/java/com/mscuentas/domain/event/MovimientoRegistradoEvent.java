package com.mscuentas.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MovimientoRegistradoEvent {

    private String movimientoId;
    private String cuentaId;
    private String clienteId;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private LocalDateTime fecha;

}
