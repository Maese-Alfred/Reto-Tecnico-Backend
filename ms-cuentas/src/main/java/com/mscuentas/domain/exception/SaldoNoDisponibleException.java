package com.mscuentas.domain.exception;

public class SaldoNoDisponibleException extends RuntimeException {

    public SaldoNoDisponibleException(String mesagge) {
        super(mesagge);
    }
}
