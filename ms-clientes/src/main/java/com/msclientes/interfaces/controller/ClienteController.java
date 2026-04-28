package com.msclientes.interfaces.controller;

import com.msclientes.application.dto.ActualizarClienteRequestDTO;
import com.msclientes.application.dto.ClienteDetalleResponseDTO;
import com.msclientes.application.dto.ClienteRequestDTO;
import com.msclientes.application.dto.ClienteResponseDTO;
import com.msclientes.application.usecase.ActualizarClienteUseCase;
import com.msclientes.application.usecase.CrearClienteUseCase;
import com.msclientes.application.usecase.EliminarClienteUseCase;
import com.msclientes.application.usecase.ObtenerClienteUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final CrearClienteUseCase crearClienteUseCase;
    private final ActualizarClienteUseCase actualizarClienteUseCase;
    private final ObtenerClienteUseCase obtenerClienteUseCase;
    private final EliminarClienteUseCase eliminarClienteUseCase;

    public ClienteController(CrearClienteUseCase crearClienteUseCase,
                             ActualizarClienteUseCase actualizarClienteUseCase,
                             ObtenerClienteUseCase obtenerClienteUseCase,
                             EliminarClienteUseCase eliminarClienteUseCase) {
        this.crearClienteUseCase = crearClienteUseCase;
        this.actualizarClienteUseCase = actualizarClienteUseCase;
        this.obtenerClienteUseCase = obtenerClienteUseCase;
        this.eliminarClienteUseCase = eliminarClienteUseCase;
    }

    @GetMapping
    public List<ClienteDetalleResponseDTO> listar() {
        return obtenerClienteUseCase.findAll();
    }

    @GetMapping("/{id}")
    public ClienteDetalleResponseDTO obtener(@PathVariable String id) {
        return obtenerClienteUseCase.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO crear(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        return crearClienteUseCase.execute(clienteRequestDTO);
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO actualizar(@PathVariable String id,
                                         @RequestBody ActualizarClienteRequestDTO dto) {
        dto.setClienteId(id);
        return actualizarClienteUseCase.execute(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        eliminarClienteUseCase.execute(id);
    }
}



