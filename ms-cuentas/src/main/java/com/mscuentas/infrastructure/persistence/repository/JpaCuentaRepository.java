package com.mscuentas.infrastructure.persistence.repository;

import com.mscuentas.infrastructure.persistence.entity.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCuentaRepository extends JpaRepository<CuentaEntity,String> {
    List<CuentaEntity> findByClienteId(String clienteId);
}
