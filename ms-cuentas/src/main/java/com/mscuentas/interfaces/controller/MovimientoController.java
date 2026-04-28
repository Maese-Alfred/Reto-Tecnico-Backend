package com.mscuentas.interfaces.controller;

import com.mscuentas.application.dto.MovimientoDetalleResponseDTO;
import com.mscuentas.application.dto.MovimientoRequestDTO;
import com.mscuentas.application.dto.MovimientoResponseDTO;
import com.mscuentas.application.usecase.ObtenerMovimientoUseCase;
import com.mscuentas.application.usecase.RegistrarMovimientoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final RegistrarMovimientoUseCase registrarMovimientoUseCase;
    private final ObtenerMovimientoUseCase obtenerMovimientoUseCase;

    public MovimientoController(RegistrarMovimientoUseCase registrarMovimientoUseCase,
                                ObtenerMovimientoUseCase obtenerMovimientoUseCase) {
        this.registrarMovimientoUseCase = registrarMovimientoUseCase;
        this.obtenerMovimientoUseCase = obtenerMovimientoUseCase;
    }

    @GetMapping("/{id}")
    public MovimientoDetalleResponseDTO obtener(@PathVariable String id) {
        return obtenerMovimientoUseCase.findById(id);
    }

    @GetMapping
    public List<MovimientoDetalleResponseDTO> listar(@RequestParam String cuentaId) {
        return obtenerMovimientoUseCase.findByCuentaId(cuentaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoResponseDTO registrar(@Valid @RequestBody MovimientoRequestDTO requestDTO) {
        return registrarMovimientoUseCase.execute(requestDTO);
    }
}
