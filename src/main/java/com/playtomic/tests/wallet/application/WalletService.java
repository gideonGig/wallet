package com.playtomic.tests.wallet.application;

import com.playtomic.tests.wallet.command.WalletCreateCommand;
import com.playtomic.tests.wallet.command.WalletTopUpWithCardCommand;
import com.playtomic.tests.wallet.dto.WalletResponseDto;
import com.playtomic.tests.wallet.dto.WalletTopUpWithCardResponse;
import com.playtomic.tests.wallet.entity.valueobject.TrackingId;

import java.util.UUID;

public interface WalletService {
    WalletResponseDto createWallet(WalletCreateCommand walletCreateCommand);

    WalletTopUpWithCardResponse cardWalletTopUp(WalletTopUpWithCardCommand walletTopUpWithCardCommand);

    WalletResponseDto findWalletByTrackingId(UUID trackingId);
}
