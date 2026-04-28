package com.mscuentas.domain.model;
import com.mscuentas.domain.exception.SaldoNoDisponibleException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Cuenta {

    private String cuentaId;
    private String numeroCuenta;
    private TipoCuenta tipoCuenta;
    private BigDecimal saldo;
    private boolean estado;
    private String clienteId;

    public Cuenta(String numeroCuenta,TipoCuenta tipoCuenta,BigDecimal saldoInicial, String clienteId){
        this.cuentaId = UUID.randomUUID().toString();
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldo = saldoInicial;
        this.estado = true;
        this.clienteId = clienteId;
    }

    public static Cuenta reconstituir(String cuentaId, String numeroCuenta, TipoCuenta tipoCuenta,
            BigDecimal saldo, boolean estado, String clienteId) {
        Cuenta c = new Cuenta(numeroCuenta, tipoCuenta, saldo, clienteId);
        c.cuentaId = cuentaId;
        c.estado = estado;
        return c;
    }

    public void retirar(BigDecimal valor){
        if(this.saldo.compareTo(valor) < 0){
            throw new SaldoNoDisponibleException("Saldo no disponible");
        }
        this.saldo = this.saldo.subtract(valor);
    }

    public void depositar(BigDecimal valor){
        this.saldo = this.saldo.add(valor);
    }

    public void desactivar() {
        this.estado = false;
    }

    public void activar() {
        this.estado = true;
    }
}
