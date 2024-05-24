package com.playtomic.tests.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class WalletTopUpWithCardResponse {
    @NonNull
    private final UUID walletTrackingId;
    @NonNull
    private final Status status;
    @NonNull
    private final String message;
}
