package com.playtomic.tests.wallet.event;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.entity.valueobject.Money;
import com.playtomic.tests.wallet.event.base.DomainEvent;

import java.time.ZonedDateTime;

public record WalletWithdrawEvent(Wallet wallet, ZonedDateTime createdAt,
                                  Money withDrawAmount) implements DomainEvent<Wallet> {

}
