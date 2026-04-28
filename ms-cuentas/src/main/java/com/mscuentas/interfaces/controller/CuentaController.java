package com.mscuentas.interfaces.controller;

import com.mscuentas.application.dto.ActualizarCuentaRequestDTO;
import com.mscuentas.application.dto.CuentaRequestDTO;
import com.mscuentas.application.dto.CuentaResponseDTO;
import com.mscuentas.application.usecase.ActualizarCuentaUseCase;
import com.mscuentas.application.usecase.CrearCuentaUseCase;
import com.mscuentas.application.usecase.EliminarCuentaUseCase;
import com.mscuentas.application.usecase.ObtenerCuentaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CrearCuentaUseCase crearCuentaUseCase;
    private final ObtenerCuentaUseCase obtenerCuentaUseCase;
    private final ActualizarCuentaUseCase actualizarCuentaUseCase;
    private final EliminarCuentaUseCase eliminarCuentaUseCase;

    public CuentaController(CrearCuentaUseCase crearCuentaUseCase,
                            ObtenerCuentaUseCase obtenerCuentaUseCase,
                            ActualizarCuentaUseCase actualizarCuentaUseCase,
                            EliminarCuentaUseCase eliminarCuentaUseCase) {
        this.crearCuentaUseCase = crearCuentaUseCase;
        this.obtenerCuentaUseCase = obtenerCuentaUseCase;
        this.actualizarCuentaUseCase = actualizarCuentaUseCase;
        this.eliminarCuentaUseCase = eliminarCuentaUseCase;
    }

    @GetMapping
    public List<CuentaResponseDTO> listar() {
        return obtenerCuentaUseCase.findAll();
    }

    @GetMapping("/{id}")
    public CuentaResponseDTO obtener(@PathVariable String id) {
        return obtenerCuentaUseCase.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaResponseDTO crear(@Valid @RequestBody CuentaRequestDTO dto) {
        return crearCuentaUseCase.execute(dto);
    }

    @PutMapping("/{id}")
    public CuentaResponseDTO actualizar(@PathVariable String id,
                                        @RequestBody ActualizarCuentaRequestDTO dto) {
        return actualizarCuentaUseCase.execute(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        eliminarCuentaUseCase.execute(id);
    }
}
