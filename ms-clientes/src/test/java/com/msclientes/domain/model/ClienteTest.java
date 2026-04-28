package com.msclientes.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Cliente - Pruebas unitarias de entidad")
class ClienteTest {

    @Test
    @DisplayName("Constructor: debe generar clienteId único, estado activo y contrasena correcta")
    void constructor_debeInicializarCorrectamente() {
        Cliente cliente = new Cliente("María López", "F", 35, "9876543210",
                "Av Siempre Viva 742", "0987654321", "miContrasena");

        assertThat(cliente.getClienteId()).isNotNull().isNotBlank();
        assertThat(cliente.getNombre()).isEqualTo("María López");
        assertThat(cliente.getGenero()).isEqualTo("F");
        assertThat(cliente.getEdad()).isEqualTo(35);
        assertThat(cliente.getIdentificacion()).isEqualTo("9876543210");
        assertThat(cliente.isEstado()).isTrue();
        assertThat(cliente.getContrasena()).isEqualTo("miContrasena");
    }

    @Test
    @DisplayName("Dos clientes creados con mismo nombre deben tener IDs distintos")
    void constructor_dosCientes_IdsDistintos() {
        Cliente c1 = new Cliente("Ana", "F", 20, "111", "Calle 1", "111", "pass");
        Cliente c2 = new Cliente("Ana", "F", 20, "111", "Calle 1", "111", "pass");

        assertThat(c1.getClienteId()).isNotEqualTo(c2.getClienteId());
    }

    @Test
    @DisplayName("desactivar: debe cambiar estado a false")
    void desactivar_debePonerEstadoFalse() {
        Cliente cliente = new Cliente("Pedro", "M", 40, "12345", "Calle 2", "222", "pass");
        assertThat(cliente.isEstado()).isTrue();

        cliente.desactivar();

        assertThat(cliente.isEstado()).isFalse();
    }

    @Test
    @DisplayName("activar: debe cambiar estado a true después de desactivar")
    void activar_debePonerEstadoTrue() {
        Cliente cliente = new Cliente("Luis", "M", 28, "54321", "Calle 3", "333", "pass");
        cliente.desactivar();
        assertThat(cliente.isEstado()).isFalse();

        cliente.activar();

        assertThat(cliente.isEstado()).isTrue();
    }

    @Test
    @DisplayName("cambiarContrasena: debe actualizar la contraseña")
    void cambiarContrasena_debeActualizarContrasena() {
        Cliente cliente = new Cliente("Carlos", "M", 32, "99999", "Calle 4", "444", "viejaPass");

        cliente.cambiarContrasena("nuevaPass");

        assertThat(cliente.getContrasena()).isEqualTo("nuevaPass");
    }

    @Test
    @DisplayName("actualizarDireccion: debe actualizar la dirección (herencia Persona)")
    void actualizarDireccion_debeActualizarCorrectamente() {
        Cliente cliente = new Cliente("Sofía", "F", 25, "11111", "Calle Vieja", "555", "pass");

        cliente.actualizarDireccion("Calle Nueva 456");

        assertThat(cliente.getDireccion()).isEqualTo("Calle Nueva 456");
    }

    @Test
    @DisplayName("reconstituir: debe preservar clienteId y estado originales")
    void reconstituir_debePreservarDatosOriginales() {
        String id = "uuid-fijo-123";
        Cliente cliente = Cliente.reconstituir(id, "Tom", "M", 22, "77777",
                "Av 9", "777", "pass", false);

        assertThat(cliente.getClienteId()).isEqualTo(id);
        assertThat(cliente.isEstado()).isFalse();
    }
}
