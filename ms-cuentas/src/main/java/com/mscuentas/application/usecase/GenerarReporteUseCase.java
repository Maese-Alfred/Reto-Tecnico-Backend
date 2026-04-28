package com.mscuentas.application.usecase;

import com.mscuentas.application.dto.CuentaReporteDTO;
import com.mscuentas.application.dto.MovimientoReporteDTO;
import com.mscuentas.application.dto.ReporteResponseDTO;
import com.mscuentas.application.dto.ReporteRequestDTO;
import com.mscuentas.domain.model.Cuenta;
import com.mscuentas.domain.model.Movimiento;
import com.mscuentas.domain.repository.CuentaRepository;
import com.mscuentas.domain.repository.MovimientoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class GenerarReporteUseCase {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public GenerarReporteUseCase(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    public ReporteResponseDTO execute(ReporteRequestDTO requestDTO){

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(requestDTO.getClienteId());

        LocalDateTime inicio = requestDTO.getFechaInicio().atStartOfDay();
        LocalDateTime fin = requestDTO.getFechaFin().atTime(23, 59, 59);

        List<CuentaReporteDTO> cuentasDTO = cuentas.stream().map(cuenta ->{

            List<Movimiento> movimientos = movimientoRepository
                    .findByCuentaIdAndFechaBetween(cuenta.getCuentaId(), inicio, fin);

            List<MovimientoReporteDTO> movimientosDTO = movimientos.stream()
                    .map(m -> new MovimientoReporteDTO(
                            m.getFecha(),
                            m.getTipoMovimiento().name(),
                            m.getValor(),
                            m.getSaldo()
                    ))
                    .collect(Collectors.toList());
            return new CuentaReporteDTO(
                    cuenta.getNumeroCuenta(),
                    cuenta.getTipoCuenta().name(),
                    cuenta.getSaldo(),
                    movimientosDTO
            );
        }).collect(Collectors.toList());
        return new ReporteResponseDTO(
                requestDTO.getClienteId(),
                cuentasDTO
        );
    }
}
