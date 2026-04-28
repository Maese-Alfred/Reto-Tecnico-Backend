package com.mscuentas.domain.repository;

import com.mscuentas.domain.model.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository {

    Cuenta save(Cuenta cuenta);

    Optional<Cuenta> findById(String cuentaId);

    List<Cuenta> findByClienteId(String clienteId);

    List<Cuenta> findAll();

    void deleteById(String cuentaId);
}
