package com.mscuentas.application.usecase;

import com.mscuentas.application.dto.MovimientoDetalleResponseDTO;
import com.mscuentas.domain.exception.DomainException;
import com.mscuentas.domain.model.Movimiento;
import com.mscuentas.domain.repository.MovimientoRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public class ObtenerMovimientoUseCase {

    private final MovimientoRepository movimientoRepository;

    public ObtenerMovimientoUseCase(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional(readOnly = true)
    public MovimientoDetalleResponseDTO findById(String movimientoId) {
        Movimiento m = movimientoRepository.findById(movimientoId)
                .orElseThrow(() -> new DomainException("Movimiento no encontrado: " + movimientoId));
        return toDTO(m);
    }

    @Transactional(readOnly = true)
    public List<MovimientoDetalleResponseDTO> findByCuentaId(String cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MovimientoDetalleResponseDTO toDTO(Movimiento m) {
        return new MovimientoDetalleResponseDTO(
                m.getMovimientoId(),
                m.getFecha(),
                m.getTipoMovimiento().name(),
                m.getValor(),
                m.getSaldo(),
                m.getCuentaId()
        );
    }
}
