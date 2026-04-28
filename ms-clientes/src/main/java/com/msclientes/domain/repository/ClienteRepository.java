package com.msclientes.domain.repository;
import com.msclientes.domain.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    Cliente save(Cliente cliente);

    Optional<Cliente> findById(String clienteId);

    Optional<Cliente> findByIdentificacion(String identificacion);

    List<Cliente> findAll();

    void delete(Cliente cliente);
}
