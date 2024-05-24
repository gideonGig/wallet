package com.playtomic.tests.wallet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class WalletResponseDto {
    private UUID walletTrackingId;
    private String Currency;
    private BigDecimal amount;
}
