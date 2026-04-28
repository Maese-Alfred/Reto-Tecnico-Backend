package com.mscuentas.interfaces.controller;


import com.mscuentas.application.dto.ReporteRequestDTO;
import com.mscuentas.application.dto.ReporteResponseDTO;
import com.mscuentas.application.usecase.GenerarReporteUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final GenerarReporteUseCase generarReporteUseCase;

    public ReporteController(GenerarReporteUseCase generarReporteUseCase){
        this.generarReporteUseCase = generarReporteUseCase;
    }

    @GetMapping
    public ReporteResponseDTO generarReporte(@ModelAttribute ReporteRequestDTO requestDTO){
        return generarReporteUseCase.execute(requestDTO);
    }
}
