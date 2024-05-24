package com.playtomic.tests.wallet.publisher;

import com.playtomic.tests.wallet.event.WalletTopUpEvent;
import com.playtomic.tests.wallet.publisher.base.DomainEventPublisher;

public interface WalletTopRequestMessagePublisher extends DomainEventPublisher<WalletTopUpEvent> {
}
