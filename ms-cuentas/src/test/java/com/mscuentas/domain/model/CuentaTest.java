package com.mscuentas.domain.model;

import com.mscuentas.domain.exception.SaldoNoDisponibleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Cuenta - Pruebas unitarias de entidad")
class CuentaTest {

    @Test
    @DisplayName("Constructor: saldo inicial correcto, estado activo, ID generado")
    void constructor_inicializaCorrectamente() {
        Cuenta cuenta = new Cuenta("001-001", TipoCuenta.AHORRO, new BigDecimal("1000.00"), "cliente-1");

        assertThat(cuenta.getCuentaId()).isNotNull();
        assertThat(cuenta.getSaldo()).isEqualByComparingTo("1000.00");
        assertThat(cuenta.isEstado()).isTrue();
        assertThat(cuenta.getClienteId()).isEqualTo("cliente-1");
    }

    @Test
    @DisplayName("depositar: incrementa el saldo correctamente")
    void depositar_incrementaSaldo() {
        Cuenta cuenta = new Cuenta("001-002", TipoCuenta.AHORRO, new BigDecimal("500.00"), "c1");

        cuenta.depositar(new BigDecimal("250.00"));

        assertThat(cuenta.getSaldo()).isEqualByComparingTo("750.00");
    }

    @Test
    @DisplayName("retirar: decrementa el saldo cuando hay fondos suficientes")
    void retirar_saldoSuficiente_decrementaSaldo() {
        Cuenta cuenta = new Cuenta("001-003", TipoCuenta.CORRIENTE, new BigDecimal("800.00"), "c2");

        cuenta.retirar(new BigDecimal("300.00"));

        assertThat(cuenta.getSaldo()).isEqualByComparingTo("500.00");
    }

    @Test
    @DisplayName("retirar: exactamente el saldo disponible → permitido")
    void retirar_exactamenteSaldoDisponible_debePermitirse() {
        Cuenta cuenta = new Cuenta("001-004", TipoCuenta.AHORRO, new BigDecimal("200.00"), "c3");

        cuenta.retirar(new BigDecimal("200.00"));

        assertThat(cuenta.getSaldo()).isEqualByComparingTo("0.00");
    }

    @Test
    @DisplayName("retirar: saldo insuficiente → lanza SaldoNoDisponibleException con mensaje correcto")
    void retirar_saldoInsuficiente_lanzaExcepcion() {
        Cuenta cuenta = new Cuenta("001-005", TipoCuenta.AHORRO, new BigDecimal("100.00"), "c4");

        assertThatThrownBy(() -> cuenta.retirar(new BigDecimal("150.00")))
                .isInstanceOf(SaldoNoDisponibleException.class)
                .hasMessageContaining("Saldo no disponible");
    }

    @Test
    @DisplayName("desactivar/activar: cambia el estado correctamente")
    void desactivarActivar_cambiasEstado() {
        Cuenta cuenta = new Cuenta("001-006", TipoCuenta.AHORRO, BigDecimal.ZERO, "c5");

        cuenta.desactivar();
        assertThat(cuenta.isEstado()).isFalse();

        cuenta.activar();
        assertThat(cuenta.isEstado()).isTrue();
    }

    @Test
    @DisplayName("reconstituir: preserva todos los campos originales")
    void reconstituir_preservaCampos() {
        String id = "fixed-uuid-001";
        Cuenta cuenta = Cuenta.reconstituir(id, "002-001", TipoCuenta.CORRIENTE,
                new BigDecimal("350.00"), false, "cliente-xyz");

        assertThat(cuenta.getCuentaId()).isEqualTo(id);
        assertThat(cuenta.isEstado()).isFalse();
        assertThat(cuenta.getSaldo()).isEqualByComparingTo("350.00");
    }
}
