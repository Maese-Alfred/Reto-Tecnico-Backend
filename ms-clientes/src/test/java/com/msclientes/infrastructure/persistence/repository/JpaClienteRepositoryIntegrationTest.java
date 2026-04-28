package com.msclientes.infrastructure.persistence.repository;

import com.msclientes.infrastructure.persistence.entity.ClienteEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("JpaClienteRepository - Prueba de integración JPA")
class JpaClienteRepositoryIntegrationTest {

    @Autowired
    private JpaClienteRepository jpaClienteRepository;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @Test
    @DisplayName("save y findById: persiste y recupera cliente correctamente")
    void saveAndFindById_debeRecuperarCorrectamente() {
        ClienteEntity entity = buildEntity("CLI-001", "7777777777");
        jpaClienteRepository.saveAndFlush(entity);

        Optional<ClienteEntity> found = jpaClienteRepository.findById("CLI-001");

        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Ana García");
        assertThat(found.get().isEstado()).isTrue();
    }

    @Test
    @DisplayName("findByIdentificacion: retorna cliente con identificación correcta")
    void findByIdentificacion_debeRetornarCliente() {
        ClienteEntity entity = buildEntity("CLI-002", "8888888888");
        jpaClienteRepository.saveAndFlush(entity);

        Optional<ClienteEntity> found = jpaClienteRepository.findByIdentificacion("8888888888");

        assertThat(found).isPresent();
        assertThat(found.get().getClienteId()).isEqualTo("CLI-002");
    }

    @Test
    @DisplayName("findByIdentificacion: identificación no existente → Optional vacío")
    void findByIdentificacion_noExiste_retornaVacio() {
        Optional<ClienteEntity> found = jpaClienteRepository.findByIdentificacion("0000000000");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("findAll: retorna todos los clientes persistidos")
    void findAll_retornaTodosLosClientes() {
        jpaClienteRepository.saveAndFlush(buildEntity("CLI-003", "1111111111"));
        jpaClienteRepository.saveAndFlush(buildEntity("CLI-004", "2222222222"));

        assertThat(jpaClienteRepository.findAll()).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("delete: elimina el cliente correctamente")
    void delete_eliminaCliente() {
        ClienteEntity entity = buildEntity("CLI-005", "3333333333");
        jpaClienteRepository.saveAndFlush(entity);

        jpaClienteRepository.deleteById("CLI-005");

        assertThat(jpaClienteRepository.findById("CLI-005")).isEmpty();
    }

    private ClienteEntity buildEntity(String id, String identificacion) {
        ClienteEntity e = new ClienteEntity();
        e.setClienteId(id);
        e.setNombre("Ana García");
        e.setGenero("F");
        e.setEdad(30);
        e.setIdentificacion(identificacion);
        e.setDireccion("Calle Test 1");
        e.setTelefono("0991234567");
        e.setContrasena("hashedPass");
        e.setEstado(true);
        return e;
    }
}
