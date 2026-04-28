package com.msclientes.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ClienteEliminadoEvent {
    private String clienteId;
    private LocalDateTime fecha;
}
