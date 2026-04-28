package com.mscuentas.application.usecase;

import com.mscuentas.application.dto.MovimientoRequestDTO;
import com.mscuentas.application.dto.MovimientoResponseDTO;
import com.mscuentas.domain.event.EventPublisher;
import com.mscuentas.domain.event.MovimientoRegistradoEvent;
import com.mscuentas.domain.model.Cuenta;
import com.mscuentas.domain.model.Movimiento;
import com.mscuentas.domain.model.TipoMovimiento;
import com.mscuentas.domain.repository.CuentaRepository;
import com.mscuentas.domain.repository.MovimientoRepository;
import com.mscuentas.domain.service.MovimientoDomainService;

import java.time.LocalDateTime;

public class RegistrarMovimientoUseCase {
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final MovimientoDomainService movimientoDomainService;
    private final EventPublisher eventPublisher;

    public RegistrarMovimientoUseCase(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository, MovimientoDomainService movimientoDomainService, EventPublisher eventPublisher) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.eventPublisher = eventPublisher;
        this.movimientoDomainService = movimientoDomainService;
    }

    public MovimientoResponseDTO execute(MovimientoRequestDTO requestDTO) {
        Cuenta cuenta = cuentaRepository.findById(requestDTO.getCuentaId())
                .orElseThrow(() -> new com.mscuentas.domain.exception.DomainException("Cuenta no encontrada: " + requestDTO.getCuentaId()));

        TipoMovimiento tipo = TipoMovimiento.valueOf(requestDTO.getTipoMovimiento());

        Movimiento movimiento = movimientoDomainService
                .registrarMovimiento(cuenta, tipo, requestDTO.getValor());

        cuentaRepository.save(cuenta);
        Movimiento saved = movimientoRepository.save(movimiento);

        MovimientoRegistradoEvent event = new MovimientoRegistradoEvent(
                saved.getMovimientoId(),
                cuenta.getCuentaId(),
                cuenta.getClienteId(),
                tipo.name(),
                requestDTO.getValor(),
                cuenta.getSaldo(),
                LocalDateTime.now()

        );
        eventPublisher.publish(event);
        return new MovimientoResponseDTO(
                saved.getMovimientoId(),
                cuenta.getSaldo()
        );
    }
}
