package com.mscuentas.application.usecase;

import com.mscuentas.domain.exception.DomainException;
import com.mscuentas.domain.model.Cuenta;
import com.mscuentas.domain.repository.CuentaRepository;

import org.springframework.transaction.annotation.Transactional;

public class EliminarCuentaUseCase {

    private final CuentaRepository cuentaRepository;

    public EliminarCuentaUseCase(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Transactional
    public void execute(String cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new DomainException("Cuenta no encontrada: " + cuentaId));
        cuenta.desactivar();
        cuentaRepository.save(cuenta);
    }
}
