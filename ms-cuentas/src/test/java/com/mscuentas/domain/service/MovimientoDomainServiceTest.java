package com.mscuentas.domain.service;

import com.mscuentas.domain.exception.SaldoNoDisponibleException;
import com.mscuentas.domain.model.Cuenta;
import com.mscuentas.domain.model.Movimiento;
import com.mscuentas.domain.model.TipoCuenta;
import com.mscuentas.domain.model.TipoMovimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MovimientoDomainService - Pruebas unitarias")
class MovimientoDomainServiceTest {

    private MovimientoDomainService service;

    @BeforeEach
    void setUp() {
        service = new MovimientoDomainService();
    }

    @Test
    @DisplayName("registrarMovimiento DEPOSITO: incrementa saldo y retorna movimiento correcto")
    void registrarMovimiento_deposito_incrementaSaldo() {
        Cuenta cuenta = new Cuenta("001", TipoCuenta.AHORRO, new BigDecimal("500.00"), "c1");
        BigDecimal valorDeposito = new BigDecimal("200.00");

        Movimiento movimiento = service.registrarMovimiento(cuenta, TipoMovimiento.DEPOSITO, valorDeposito);

        assertThat(cuenta.getSaldo()).isEqualByComparingTo("700.00");
        assertThat(movimiento.getTipoMovimiento()).isEqualTo(TipoMovimiento.DEPOSITO);
        assertThat(movimiento.getValor()).isEqualByComparingTo("200.00");
        assertThat(movimiento.getSaldo()).isEqualByComparingTo("700.00");
        assertThat(movimiento.getMovimientoId()).isNotNull();
    }

    @Test
    @DisplayName("registrarMovimiento RETIRO: reduce saldo correctamente")
    void registrarMovimiento_retiro_reduceSaldo() {
        Cuenta cuenta = new Cuenta("002", TipoCuenta.CORRIENTE, new BigDecimal("1000.00"), "c2");

        Movimiento movimiento = service.registrarMovimiento(cuenta, TipoMovimiento.RETIRO, new BigDecimal("400.00"));

        assertThat(cuenta.getSaldo()).isEqualByComparingTo("600.00");
        assertThat(movimiento.getTipoMovimiento()).isEqualTo(TipoMovimiento.RETIRO);
        assertThat(movimiento.getSaldo()).isEqualByComparingTo("600.00");
    }

    @Test
    @DisplayName("registrarMovimiento RETIRO con saldo insuficiente: lanza SaldoNoDisponibleException")
    void registrarMovimiento_retiro_saldoInsuficiente_lanzaExcepcion() {
        Cuenta cuenta = new Cuenta("003", TipoCuenta.AHORRO, new BigDecimal("100.00"), "c3");

        assertThatThrownBy(() ->
                service.registrarMovimiento(cuenta, TipoMovimiento.RETIRO, new BigDecimal("500.00"))
        )
                .isInstanceOf(SaldoNoDisponibleException.class)
                .hasMessageContaining("Saldo no disponible");
    }

    @Test
    @DisplayName("registrarMovimiento RETIRO exacto al saldo: debe ejecutarse exitosamente")
    void registrarMovimiento_retiro_exactamenteElSaldo_exitoso() {
        Cuenta cuenta = new Cuenta("004", TipoCuenta.AHORRO, new BigDecimal("250.00"), "c4");

        Movimiento movimiento = service.registrarMovimiento(cuenta, TipoMovimiento.RETIRO, new BigDecimal("250.00"));

        assertThat(cuenta.getSaldo()).isEqualByComparingTo("0.00");
        assertThat(movimiento.getSaldo()).isEqualByComparingTo("0.00");
    }

    @Test
    @DisplayName("registrarMovimiento múltiples depósitos: saldo acumulado correcto")
    void registrarMovimiento_multiplesDepositos_saldoAcumulado() {
        Cuenta cuenta = new Cuenta("005", TipoCuenta.AHORRO, BigDecimal.ZERO, "c5");

        service.registrarMovimiento(cuenta, TipoMovimiento.DEPOSITO, new BigDecimal("300.00"));
        service.registrarMovimiento(cuenta, TipoMovimiento.DEPOSITO, new BigDecimal("150.00"));

        assertThat(cuenta.getSaldo()).isEqualByComparingTo("450.00");
    }
}
