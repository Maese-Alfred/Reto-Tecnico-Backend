package com.mscuentas.domain.event;

public interface EventPublisher {

    void publish(Object event);
}
