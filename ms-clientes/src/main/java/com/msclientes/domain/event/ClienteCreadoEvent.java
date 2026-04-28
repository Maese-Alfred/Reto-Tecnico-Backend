package com.msclientes.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ClienteCreadoEvent {
    private String clienteId;
    private String identificacion;
    private LocalDateTime fecha;
}
