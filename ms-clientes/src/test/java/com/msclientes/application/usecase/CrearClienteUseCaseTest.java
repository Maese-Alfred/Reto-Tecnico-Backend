package com.msclientes.application.usecase;

import com.msclientes.application.dto.ClienteRequestDTO;
import com.msclientes.application.dto.ClienteResponseDTO;
import com.msclientes.domain.event.EventPublisher;
import com.msclientes.domain.exception.DomainException;
import com.msclientes.domain.model.Cliente;
import com.msclientes.domain.repository.ClienteRepository;
import com.msclientes.domain.service.ClienteDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CrearClienteUseCase - Pruebas unitarias")
class CrearClienteUseCaseTest {

    @Mock private ClienteRepository clienteRepository;
    @Mock private EventPublisher eventPublisher;

    private ClienteDomainService clienteDomainService;
    private CrearClienteUseCase crearClienteUseCase;

    @BeforeEach
    void setUp() {
        clienteDomainService = new ClienteDomainService(clienteRepository);
        crearClienteUseCase = new CrearClienteUseCase(clienteRepository, clienteDomainService, eventPublisher);
    }

    @Test
    @DisplayName("execute: cliente válido → retorna DTO y publica evento")
    void execute_clienteValido_retornaDTOyPublicaEvento() {
        ClienteRequestDTO request = buildRequest("1234567890");
        given(clienteRepository.findByIdentificacion("1234567890")).willReturn(Optional.empty());
        given(clienteRepository.save(any(Cliente.class))).willAnswer(inv -> inv.getArgument(0));

        ClienteResponseDTO result = crearClienteUseCase.execute(request);

        assertThat(result.getNombre()).isEqualTo("Juan Pérez");
        assertThat(result.isEstado()).isTrue();
        assertThat(result.getClienteId()).isNotNull();
        verify(eventPublisher).publish(any());
    }

    @Test
    @DisplayName("execute: identificación duplicada → lanza DomainException, no publica evento")
    void execute_identificacionDuplicada_lanzaExcepcion() {
        ClienteRequestDTO request = buildRequest("1234567890");
        Cliente existente = new Cliente("Otro", "M", 30, "1234567890", "Av 1", "000", "p");
        given(clienteRepository.findByIdentificacion("1234567890")).willReturn(Optional.of(existente));

        assertThatThrownBy(() -> crearClienteUseCase.execute(request))
                .isInstanceOf(DomainException.class);
    }

    private ClienteRequestDTO buildRequest(String identificacion) {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNombre("Juan Pérez");
        dto.setGenero("M");
        dto.setEdad(30);
        dto.setIdentificacion(identificacion);
        dto.setDireccion("Calle 1");
        dto.setTelefono("0991234567");
        dto.setContrasena("pass123");
        return dto;
    }
}
