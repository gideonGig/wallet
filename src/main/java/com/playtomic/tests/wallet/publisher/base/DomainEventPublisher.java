package com.playtomic.tests.wallet.publisher.base;

import com.playtomic.tests.wallet.event.base.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}