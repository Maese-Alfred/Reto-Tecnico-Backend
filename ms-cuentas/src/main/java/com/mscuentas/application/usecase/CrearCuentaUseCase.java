package com.mscuentas.application.usecase;

import com.mscuentas.application.dto.CuentaRequestDTO;
import com.mscuentas.application.dto.CuentaResponseDTO;
import com.mscuentas.domain.exception.DomainException;
import com.mscuentas.domain.model.Cuenta;
import com.mscuentas.domain.model.TipoCuenta;
import com.mscuentas.domain.repository.CuentaRepository;

import org.springframework.transaction.annotation.Transactional;

public class CrearCuentaUseCase {

    private final CuentaRepository cuentaRepository;

    public CrearCuentaUseCase(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Transactional
    public CuentaResponseDTO execute(CuentaRequestDTO dto) {
        TipoCuenta tipo;
        try {
            tipo = TipoCuenta.valueOf(dto.getTipoCuenta().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DomainException("Tipo de cuenta inválido: " + dto.getTipoCuenta());
        }

        Cuenta cuenta = new Cuenta(dto.getNumeroCuenta(), tipo, dto.getSaldoInicial(), dto.getClienteId());
        Cuenta saved = cuentaRepository.save(cuenta);
        return toDTO(saved);
    }

    static CuentaResponseDTO toDTO(Cuenta c) {
        return new CuentaResponseDTO(
                c.getCuentaId(),
                c.getNumeroCuenta(),
                c.getTipoCuenta().name(),
                c.getSaldo(),
                c.isEstado(),
                c.getClienteId()
        );
    }
}
