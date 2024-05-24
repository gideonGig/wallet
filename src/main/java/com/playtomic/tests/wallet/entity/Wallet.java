package com.playtomic.tests.wallet.entity;

import com.playtomic.tests.wallet.entity.base.AggregateRoot;
import com.playtomic.tests.wallet.entity.valueobject.*;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Wallet extends AggregateRoot<WalletId> {
    @EmbeddedId
    private WalletId walletId;

    @Embedded
    private Money balance;

    @Embedded
    private WalletName name;

    @Embedded
    private TrackingId trackingId;

    @Embedded
    private CustomerId customerId;

    @Version
    private long version;

    protected Wallet() {


    }

    public Wallet(Money balance, WalletName name, TrackingId trackingId, CustomerId customerId) {
        this.walletId = new WalletId(UUID.randomUUID());
        this.balance = balance;
        this.name = name;
        this.trackingId = trackingId;
        this.customerId = customerId;
    }

    @Override
    public WalletId getId() {
        return walletId;
    }

}
