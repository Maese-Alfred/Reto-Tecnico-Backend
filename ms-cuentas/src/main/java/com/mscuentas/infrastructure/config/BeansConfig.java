package com.mscuentas.infrastructure.config;

import com.mscuentas.application.usecase.*;
import com.mscuentas.domain.event.EventPublisher;
import com.mscuentas.domain.repository.CuentaRepository;
import com.mscuentas.domain.repository.MovimientoRepository;
import com.mscuentas.domain.service.MovimientoDomainService;
import com.mscuentas.infrastructure.messaging.RabbitEventPublisher;
import com.mscuentas.infrastructure.persistence.adapter.CuentaRepositoryAdapter;
import com.mscuentas.infrastructure.persistence.adapter.MovimientoRepositoryAdapter;
import com.mscuentas.infrastructure.persistence.repository.JpaCuentaRepository;
import com.mscuentas.infrastructure.persistence.repository.JpaMovimientoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public CuentaRepository cuentaRepository(JpaCuentaRepository jpaCuentaRepository) {
        return new CuentaRepositoryAdapter(jpaCuentaRepository);
    }

    @Bean
    public MovimientoRepository movimientoRepository(JpaMovimientoRepository jpaMovimientoRepository) {
        return new MovimientoRepositoryAdapter(jpaMovimientoRepository);
    }

    @Bean
    public MovimientoDomainService movimientoDomainService() {
        return new MovimientoDomainService();
    }

    @Bean
    public EventPublisher eventPublisher(RabbitTemplate rabbitTemplate) {
        return new RabbitEventPublisher(rabbitTemplate);
    }

    @Bean
    public CrearCuentaUseCase crearCuentaUseCase(CuentaRepository cuentaRepository) {
        return new CrearCuentaUseCase(cuentaRepository);
    }

    @Bean
    public ObtenerCuentaUseCase obtenerCuentaUseCase(CuentaRepository cuentaRepository) {
        return new ObtenerCuentaUseCase(cuentaRepository);
    }

    @Bean
    public ActualizarCuentaUseCase actualizarCuentaUseCase(CuentaRepository cuentaRepository) {
        return new ActualizarCuentaUseCase(cuentaRepository);
    }

    @Bean
    public EliminarCuentaUseCase eliminarCuentaUseCase(CuentaRepository cuentaRepository) {
        return new EliminarCuentaUseCase(cuentaRepository);
    }

    @Bean
    public RegistrarMovimientoUseCase registrarMovimientoUseCase(
            CuentaRepository cuentaRepository,
            MovimientoRepository movimientoRepository,
            MovimientoDomainService movimientoDomainService,
            EventPublisher eventPublisher) {
        return new RegistrarMovimientoUseCase(
                cuentaRepository,
                movimientoRepository,
                movimientoDomainService,
                eventPublisher
        );
    }

    @Bean
    public ObtenerMovimientoUseCase obtenerMovimientoUseCase(MovimientoRepository movimientoRepository) {
        return new ObtenerMovimientoUseCase(movimientoRepository);
    }

    @Bean
    public GenerarReporteUseCase generarReporteUseCase(
            CuentaRepository cuentaRepository,
            MovimientoRepository movimientoRepository) {
        return new GenerarReporteUseCase(cuentaRepository, movimientoRepository);
    }
}
