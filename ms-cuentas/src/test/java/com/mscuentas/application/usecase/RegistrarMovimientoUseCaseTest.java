package com.mscuentas.application.usecase;

import com.mscuentas.application.dto.MovimientoRequestDTO;
import com.mscuentas.application.dto.MovimientoResponseDTO;
import com.mscuentas.domain.event.EventPublisher;
import com.mscuentas.domain.exception.DomainException;
import com.mscuentas.domain.exception.SaldoNoDisponibleException;
import com.mscuentas.domain.model.Cuenta;
import com.mscuentas.domain.model.Movimiento;
import com.mscuentas.domain.model.TipoCuenta;
import com.mscuentas.domain.model.TipoMovimiento;
import com.mscuentas.domain.repository.CuentaRepository;
import com.mscuentas.domain.repository.MovimientoRepository;
import com.mscuentas.domain.service.MovimientoDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrarMovimientoUseCase - Pruebas unitarias")
class RegistrarMovimientoUseCaseTest {

    @Mock private CuentaRepository cuentaRepository;
    @Mock private MovimientoRepository movimientoRepository;
    @Mock private EventPublisher eventPublisher;

    private MovimientoDomainService movimientoDomainService;
    private RegistrarMovimientoUseCase useCase;

    @BeforeEach
    void setUp() {
        movimientoDomainService = new MovimientoDomainService();
        useCase = new RegistrarMovimientoUseCase(cuentaRepository, movimientoRepository,
                movimientoDomainService, eventPublisher);
    }

    @Test
    @DisplayName("execute DEPOSITO: retorna saldo actualizado y publica evento")
    void execute_deposito_actualizaSaldoYPublicaEvento() {
        Cuenta cuenta = Cuenta.reconstituir("cuenta-1", "001", TipoCuenta.AHORRO,
                new BigDecimal("500.00"), true, "cli-1");

        given(cuentaRepository.findById("cuenta-1")).willReturn(Optional.of(cuenta));
        given(cuentaRepository.save(any())).willAnswer(inv -> inv.getArgument(0));
        given(movimientoRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        MovimientoRequestDTO dto = buildRequest("cuenta-1", "DEPOSITO", "200.00");
        MovimientoResponseDTO result = useCase.execute(dto);

        assertThat(result.getSaldoDisponible()).isEqualByComparingTo("700.00");
        verify(eventPublisher).publish(any());
        verify(cuentaRepository).save(any());
    }

    @Test
    @DisplayName("execute RETIRO: saldo suficiente → reduce saldo correctamente")
    void execute_retiro_saldoSuficiente_reduceSaldo() {
        Cuenta cuenta = Cuenta.reconstituir("cuenta-2", "002", TipoCuenta.CORRIENTE,
                new BigDecimal("1000.00"), true, "cli-2");

        given(cuentaRepository.findById("cuenta-2")).willReturn(Optional.of(cuenta));
        given(cuentaRepository.save(any())).willAnswer(inv -> inv.getArgument(0));
        given(movimientoRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        MovimientoRequestDTO dto = buildRequest("cuenta-2", "RETIRO", "400.00");
        MovimientoResponseDTO result = useCase.execute(dto);

        assertThat(result.getSaldoDisponible()).isEqualByComparingTo("600.00");
    }

    @Test
    @DisplayName("execute RETIRO: saldo insuficiente → lanza SaldoNoDisponibleException")
    void execute_retiro_saldoInsuficiente_lanzaExcepcion() {
        Cuenta cuenta = Cuenta.reconstituir("cuenta-3", "003", TipoCuenta.AHORRO,
                new BigDecimal("50.00"), true, "cli-3");

        given(cuentaRepository.findById("cuenta-3")).willReturn(Optional.of(cuenta));

        MovimientoRequestDTO dto = buildRequest("cuenta-3", "RETIRO", "200.00");

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(SaldoNoDisponibleException.class)
                .hasMessageContaining("Saldo no disponible");
    }

    @Test
    @DisplayName("execute: cuenta inexistente → lanza DomainException")
    void execute_cuentaInexistente_lanzaDomainException() {
        given(cuentaRepository.findById("no-existe")).willReturn(Optional.empty());

        MovimientoRequestDTO dto = buildRequest("no-existe", "DEPOSITO", "100.00");

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("execute: tipo de movimiento inválido → lanza IllegalArgumentException")
    void execute_tipoInvalido_lanzaExcepcion() {
        Cuenta cuenta = Cuenta.reconstituir("cuenta-4", "004", TipoCuenta.AHORRO,
                new BigDecimal("100.00"), true, "cli-4");
        given(cuentaRepository.findById("cuenta-4")).willReturn(Optional.of(cuenta));

        MovimientoRequestDTO dto = buildRequest("cuenta-4", "TRANSFERENCIA", "50.00");

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private MovimientoRequestDTO buildRequest(String cuentaId, String tipo, String valor) {
        MovimientoRequestDTO dto = new MovimientoRequestDTO();
        dto.setCuentaId(cuentaId);
        dto.setTipoMovimiento(tipo);
        dto.setValor(new BigDecimal(valor));
        return dto;
    }
}
