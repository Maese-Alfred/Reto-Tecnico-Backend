package com.mscuentas.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mscuentas.application.dto.CuentaRequestDTO;
import com.mscuentas.application.dto.MovimientoRequestDTO;
import com.mscuentas.infrastructure.persistence.entity.CuentaEntity;
import com.mscuentas.infrastructure.persistence.repository.JpaCuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("MovimientoController - Pruebas de integración")
class MovimientoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaCuentaRepository jpaCuentaRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    private static final String CUENTA_ID = "cuenta-integ-01";

    @BeforeEach
    void setUp() {
        if (!jpaCuentaRepository.existsById(CUENTA_ID)) {
            CuentaEntity cuenta = new CuentaEntity();
            cuenta.setCuentaId(CUENTA_ID);
            cuenta.setNumeroCuenta("0991-INTEG");
            cuenta.setTipoCuenta("AHORRO");
            cuenta.setSaldo(new BigDecimal("500.00"));
            cuenta.setEstado(true);
            cuenta.setClienteId("cliente-integ-01");
            jpaCuentaRepository.save(cuenta);
        }
    }

    @Test
    @DisplayName("POST /movimientos DEPOSITO: cuenta válida → 201 y saldo incrementado")
    void postMovimiento_deposito_retorna201() throws Exception {
        MovimientoRequestDTO dto = buildRequest(CUENTA_ID, "DEPOSITO", "200.00");

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.saldoDisponible").value(700.00));
    }

    @Test
    @DisplayName("POST /movimientos RETIRO: saldo insuficiente → 409 Conflict")
    void postMovimiento_retiro_saldoInsuficiente_retorna409() throws Exception {
        MovimientoRequestDTO dto = buildRequest(CUENTA_ID, "RETIRO", "9999.00");

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("saldo")));
    }

    @Test
    @DisplayName("POST /movimientos: cuenta inexistente → 400")
    void postMovimiento_cuentaNoExistente_retorna400() throws Exception {
        MovimientoRequestDTO dto = buildRequest("no-existe-cuenta", "DEPOSITO", "100.00");

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /movimientos: campos inválidos → 400 Bad Request")
    void postMovimiento_camposInvalidos_retorna400() throws Exception {
        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /cuentas: crea cuenta → 201")
    void crearCuenta_valida_retorna201() throws Exception {
        CuentaRequestDTO dto = new CuentaRequestDTO();
        dto.setNumeroCuenta("999-NEW");
        dto.setTipoCuenta("CORRIENTE");
        dto.setSaldoInicial(new BigDecimal("0.00"));
        dto.setClienteId("cliente-nuevo-01");

        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cuentaId").isNotEmpty());
    }

    private MovimientoRequestDTO buildRequest(String cuentaId, String tipo, String valor) {
        MovimientoRequestDTO dto = new MovimientoRequestDTO();
        dto.setCuentaId(cuentaId);
        dto.setTipoMovimiento(tipo);
        dto.setValor(new BigDecimal(valor));
        return dto;
    }
}
