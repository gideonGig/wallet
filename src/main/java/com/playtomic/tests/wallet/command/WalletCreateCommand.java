package com.playtomic.tests.wallet.command;

import com.playtomic.tests.wallet.entity.valueobject.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
@AllArgsConstructor
public class WalletCreateCommand {
    @NonNull
    private Currency currency;
    @NonNull
    private String customerId;
    private String walletName;
}
