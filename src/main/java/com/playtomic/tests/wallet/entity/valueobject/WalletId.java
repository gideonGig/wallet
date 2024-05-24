package com.playtomic.tests.wallet.entity.valueobject;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class WalletId implements Serializable {
    private UUID id;

    protected WalletId() {
        // Required by JPA
    }

    public WalletId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletId walletId = (WalletId) o;
        return Objects.equals(id, walletId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

