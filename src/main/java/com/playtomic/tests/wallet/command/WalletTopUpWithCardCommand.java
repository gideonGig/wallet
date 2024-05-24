package com.playtomic.tests.wallet.command;

import com.playtomic.tests.wallet.entity.valueobject.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class WalletTopUpWithCardCommand {
    @NotNull
    private String cardNumber;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Currency currency;
    @NotNull
    private UUID walletTrackingId;
}
