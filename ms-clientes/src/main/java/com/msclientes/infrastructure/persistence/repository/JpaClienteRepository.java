package com.msclientes.infrastructure.persistence.repository;

import com.msclientes.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaClienteRepository extends JpaRepository<ClienteEntity,String> {

    Optional<ClienteEntity> findByIdentificacion(String identificacion);

}
