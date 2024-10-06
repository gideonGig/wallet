package com.playtomic.tests.wallet.entity.valueobject;

import java.io.Serializable;
import java.util.Objects;

public class WalletName implements Serializable {
    private String walletName;

    public WalletName(String walletName) {
        this.walletName = walletName;
    }

    protected WalletName() {
        // Required by JPA
    }

    public String getWalletName() {
        return walletName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletName walletName = (WalletName) o;
        return Objects.equals(walletName, walletName.walletName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletName);
    }
}
