package com.mscuentas.application.usecase;

import com.mscuentas.application.dto.CuentaResponseDTO;
import com.mscuentas.domain.exception.DomainException;
import com.mscuentas.domain.model.Cuenta;
import com.mscuentas.domain.repository.CuentaRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public class ObtenerCuentaUseCase {

    private final CuentaRepository cuentaRepository;

    public ObtenerCuentaUseCase(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Transactional(readOnly = true)
    public CuentaResponseDTO findById(String cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new DomainException("Cuenta no encontrada: " + cuentaId));
        return CrearCuentaUseCase.toDTO(cuenta);
    }

    @Transactional(readOnly = true)
    public List<CuentaResponseDTO> findAll() {
        return cuentaRepository.findAll().stream()
                .map(CrearCuentaUseCase::toDTO)
                .collect(Collectors.toList());
    }
}
