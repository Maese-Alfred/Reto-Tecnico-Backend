package com.msclientes.infrastructure.config;

import com.msclientes.application.usecase.ActualizarClienteUseCase;
import com.msclientes.application.usecase.CrearClienteUseCase;
import com.msclientes.application.usecase.EliminarClienteUseCase;
import com.msclientes.application.usecase.ObtenerClienteUseCase;
import com.msclientes.domain.event.EventPublisher;
import com.msclientes.domain.repository.ClienteRepository;
import com.msclientes.domain.service.ClienteDomainService;
import com.msclientes.infrastructure.messaging.RabbitEventPublisher;
import com.msclientes.infrastructure.persistence.adapter.ClienteRepositoryAdapter;
import com.msclientes.infrastructure.persistence.repository.JpaClienteRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public ClienteRepository clienteRepository(JpaClienteRepository jpaRepository) {
        return new ClienteRepositoryAdapter(jpaRepository);
    }

    @Bean
    public ClienteDomainService clienteDomainService(ClienteRepository clienteRepository) {
        return new ClienteDomainService(clienteRepository);
    }

    @Bean
    public EventPublisher eventPublisher(RabbitTemplate rabbitTemplate) {
        return new RabbitEventPublisher(rabbitTemplate);
    }

    @Bean
    public CrearClienteUseCase crearClienteUseCase(ClienteRepository clienteRepository,
                                                   ClienteDomainService clienteDomainService,
                                                   EventPublisher eventPublisher) {
        return new CrearClienteUseCase(clienteRepository, clienteDomainService, eventPublisher);
    }

    @Bean
    public ActualizarClienteUseCase actualizarClienteUseCase(ClienteRepository clienteRepository) {
        return new ActualizarClienteUseCase(clienteRepository);
    }

    @Bean
    public ObtenerClienteUseCase obtenerClienteUseCase(ClienteRepository clienteRepository) {
        return new ObtenerClienteUseCase(clienteRepository);
    }

    @Bean
    public EliminarClienteUseCase eliminarClienteUseCase(ClienteRepository clienteRepository,
                                                         EventPublisher eventPublisher) {
        return new EliminarClienteUseCase(clienteRepository, eventPublisher);
    }
}
