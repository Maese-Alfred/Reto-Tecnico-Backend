package com.msclientes.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msclientes.application.dto.ClienteRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("ClienteController - Pruebas de integración")
class ClienteControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @DisplayName("POST /clientes: crea cliente → 201 y retorna datos")
    void crearCliente_valido_retorna201() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest("Juan Lopez", "1234567890"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan Lopez"));
    }

    @Test
    @DisplayName("POST /clientes: identificación duplicada → 409 Conflict")
    void crearCliente_identificacionDuplicada_retorna409() throws Exception {
        ClienteRequestDTO dto = buildRequest("María Paz", "9988776655");
        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /clientes: campos faltantes → 400 Bad Request")
    void crearCliente_camposFaltantes_retorna400() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /clientes: lista todos los clientes")
    void listarClientes_retorna200() throws Exception {
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /clientes/{id}: cliente inexistente → 400 o 404")
    void obtenerCliente_noExiste_retornaError() throws Exception {
        mockMvc.perform(get("/clientes/no-existe-id"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("DELETE /clientes/{id}: cliente inexistente → error cliente")
    void eliminarCliente_noExiste_retornaError() throws Exception {
        mockMvc.perform(delete("/clientes/no-existe"))
                .andExpect(status().is4xxClientError());
    }

    private ClienteRequestDTO buildRequest(String nombre, String identificacion) {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNombre(nombre);
        dto.setGenero("M");
        dto.setEdad(28);
        dto.setIdentificacion(identificacion);
        dto.setDireccion("Av. Principal 100");
        dto.setTelefono("0991234567");
        dto.setContrasena("Pass1234");
        return dto;
    }
}
