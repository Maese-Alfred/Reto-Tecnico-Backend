package com.mscuentas.infrastructure.persistence.repository;

import com.mscuentas.infrastructure.persistence.entity.CuentaEntity;
import com.mscuentas.infrastructure.persistence.entity.MovimientoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("JpaCuentaRepository + JpaMovimientoRepository - Pruebas de integración JPA")
class CuentaMovimientoRepositoryIntegrationTest {

    @Autowired
    private JpaCuentaRepository jpaCuentaRepository;

    @Autowired
    private JpaMovimientoRepository jpaMovimientoRepository;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @Test
    @DisplayName("findByClienteId: retorna cuentas del cliente")
    void findByClienteId_retornaCuentasDelCliente() {
        jpaCuentaRepository.saveAndFlush(buildCuenta("C-001", "CL-A"));
        jpaCuentaRepository.saveAndFlush(buildCuenta("C-002", "CL-A"));
        jpaCuentaRepository.saveAndFlush(buildCuenta("C-003", "CL-B"));

        List<CuentaEntity> cuentas = jpaCuentaRepository.findByClienteId("CL-A");

        assertThat(cuentas).hasSize(2);
        assertThat(cuentas).allMatch(c -> "CL-A".equals(c.getClienteId()));
    }

    @Test
    @DisplayName("findByClienteId: cliente sin cuentas → lista vacía")
    void findByClienteId_sinCuentas_listaVacia() {
        assertThat(jpaCuentaRepository.findByClienteId("CL-SIN-CUENTAS")).isEmpty();
    }

    @Test
    @DisplayName("findByCuentaId: retorna movimientos de la cuenta")
    void findByCuentaId_retornaMovimientos() {
        jpaCuentaRepository.saveAndFlush(buildCuenta("C-010", "CL-X"));
        jpaMovimientoRepository.saveAndFlush(buildMovimiento("M-001", "C-010"));
        jpaMovimientoRepository.saveAndFlush(buildMovimiento("M-002", "C-010"));

        List<MovimientoEntity> movimientos = jpaMovimientoRepository.findByCuentaId("C-010");

        assertThat(movimientos).hasSize(2);
    }

    @Test
    @DisplayName("findByCuentaIdAndFechaBetween: filtra movimientos por rango de fecha")
    void findByCuentaIdAndFechaBetween_filtraPorFechas() {
        jpaCuentaRepository.saveAndFlush(buildCuenta("C-020", "CL-Y"));

        MovimientoEntity mov1 = buildMovimiento("M-010", "C-020");
        mov1.setFecha(LocalDateTime.of(2024, 1, 10, 0, 0));
        MovimientoEntity mov2 = buildMovimiento("M-011", "C-020");
        mov2.setFecha(LocalDateTime.of(2024, 3, 15, 0, 0));
        MovimientoEntity mov3 = buildMovimiento("M-012", "C-020");
        mov3.setFecha(LocalDateTime.of(2024, 6, 1, 0, 0));

        jpaMovimientoRepository.saveAndFlush(mov1);
        jpaMovimientoRepository.saveAndFlush(mov2);
        jpaMovimientoRepository.saveAndFlush(mov3);

        LocalDateTime desde = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime hasta = LocalDateTime.of(2024, 4, 1, 0, 0);

        List<MovimientoEntity> result = jpaMovimientoRepository
                .findByCuentaIdAndFechaBetween("C-020", desde, hasta);

        assertThat(result).hasSize(2);
    }

    private CuentaEntity buildCuenta(String cuentaId, String clienteId) {
        CuentaEntity e = new CuentaEntity();
        e.setCuentaId(cuentaId);
        e.setNumeroCuenta("NUM-" + cuentaId);
        e.setTipoCuenta("AHORRO");
        e.setSaldo(new BigDecimal("1000.00"));
        e.setEstado(true);
        e.setClienteId(clienteId);
        return e;
    }

    private MovimientoEntity buildMovimiento(String movimientoId, String cuentaId) {
        MovimientoEntity e = new MovimientoEntity();
        e.setMovimientoId(movimientoId);
        e.setFecha(LocalDateTime.now());
        e.setTipoMovimiento("DEPOSITO");
        e.setValor(new BigDecimal("100.00"));
        e.setSaldo(new BigDecimal("1100.00"));
        e.setCuentaId(cuentaId);
        return e;
    }
}
