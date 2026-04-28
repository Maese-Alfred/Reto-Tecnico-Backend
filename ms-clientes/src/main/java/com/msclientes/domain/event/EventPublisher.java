package com.msclientes.domain.event;


public interface EventPublisher {

    void publish(Object event);
}
