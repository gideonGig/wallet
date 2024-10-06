package com.playtomic.tests.wallet.publisher;

import com.playtomic.tests.wallet.event.WalletTopUpEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WalletTopUpRequestDomainMessagePublisherImpl implements WalletTopRequestMessagePublisher {
    @Override
    public void publish(WalletTopUpEvent domainEvent) {
        /**
         * we can use a generic Event Bus like kafka, etc, to
         * publish our events, this event would be the TransactionService
         * service to create Immutable objects of all WalletTop and WalletWithdrawal
         */
        log.info(domainEvent.toString());
    }
}
