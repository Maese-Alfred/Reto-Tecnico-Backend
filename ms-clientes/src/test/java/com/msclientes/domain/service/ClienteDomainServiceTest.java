package com.msclientes.domain.service;

import com.msclientes.domain.exception.DomainException;
import com.msclientes.domain.model.Cliente;
import com.msclientes.domain.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteDomainService - Pruebas unitarias")
class ClienteDomainServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteDomainService clienteDomainService;

    private Cliente clienteBase;

    @BeforeEach
    void setUp() {
        clienteBase = new Cliente("Juan Pérez", "M", 30, "1234567890",
                "Calle Falsa 123", "0991234567", "securePass");
    }

    @Test
    @DisplayName("crearCliente: identificación nueva → no lanza excepción")
    void crearCliente_identificacionNueva_noLanzaExcepcion() {
        given(clienteRepository.findByIdentificacion("1234567890")).willReturn(Optional.empty());

        assertThatNoException().isThrownBy(() -> clienteDomainService.crearCliente(clienteBase));
    }

    @Test
    @DisplayName("crearCliente: identificación duplicada → lanza DomainException")
    void crearCliente_identificacionDuplicada_lanzaDomainException() {
        Cliente existente = new Cliente("Pedro", "M", 25, "1234567890", "Av 2", "555", "pass");
        given(clienteRepository.findByIdentificacion("1234567890")).willReturn(Optional.of(existente));

        assertThatThrownBy(() -> clienteDomainService.crearCliente(clienteBase))
                .isInstanceOf(DomainException.class)
                .hasMessage("El cliente existe en el sistema");
    }

    @Test
    @DisplayName("validarUnicidadIdentificacion: identificación no registrada → sin excepción")
    void validarUnicidadIdentificacion_noExiste_sinExcepcion() {
        given(clienteRepository.findByIdentificacion("9999999999")).willReturn(Optional.empty());

        assertThatNoException()
                .isThrownBy(() -> clienteDomainService.validarUnicidadIdentificacion("9999999999"));
    }

    @Test
    @DisplayName("validarUnicidadIdentificacion: identificación ya registrada → lanza DomainException")
    void validarUnicidadIdentificacion_yaExiste_lanzaExcepcion() {
        Cliente existente = new Cliente("Ana", "F", 28, "9999999999", "Av 5", "333", "p");
        given(clienteRepository.findByIdentificacion("9999999999")).willReturn(Optional.of(existente));

        assertThatThrownBy(() -> clienteDomainService.validarUnicidadIdentificacion("9999999999"))
                .isInstanceOf(DomainException.class);
    }
}
