package com.mscuentas.application.usecase;

import com.mscuentas.application.dto.ActualizarCuentaRequestDTO;
import com.mscuentas.application.dto.CuentaResponseDTO;
import com.mscuentas.domain.exception.DomainException;
import com.mscuentas.domain.model.Cuenta;
import com.mscuentas.domain.model.TipoCuenta;
import com.mscuentas.domain.repository.CuentaRepository;

import org.springframework.transaction.annotation.Transactional;

public class ActualizarCuentaUseCase {

    private final CuentaRepository cuentaRepository;

    public ActualizarCuentaUseCase(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Transactional
    public CuentaResponseDTO execute(String cuentaId, ActualizarCuentaRequestDTO dto) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new DomainException("Cuenta no encontrada: " + cuentaId));

        if (dto.getEstado() != null) {
            if (dto.getEstado()) cuenta.activar();
            else cuenta.desactivar();
        }

        Cuenta updated = cuentaRepository.save(cuenta);
        return CrearCuentaUseCase.toDTO(updated);
    }
}
