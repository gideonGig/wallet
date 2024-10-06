package com.playtomic.tests.wallet.application;

import com.playtomic.tests.wallet.command.WalletCreateCommand;
import com.playtomic.tests.wallet.command.WalletTopUpWithCardCommand;
import com.playtomic.tests.wallet.commandhandler.WalletCreateCommandHandler;
import com.playtomic.tests.wallet.commandhandler.WalletTopUpCommandHandler;
import com.playtomic.tests.wallet.dto.WalletResponseDto;
import com.playtomic.tests.wallet.dto.WalletTopUpWithCardResponse;
import com.playtomic.tests.wallet.query.WalletQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class WalletServiceImpl implements WalletService {
    private final WalletTopUpCommandHandler walletTopUpCommandHandler;
    private final WalletCreateCommandHandler walletCreateCommandHandler;
    private final WalletQueryService walletQueryService;

    public WalletServiceImpl (WalletTopUpCommandHandler walletTopUpCommandHandler,
                             WalletCreateCommandHandler walletCreateCommandHandler, WalletQueryService walletQueryService) {
        this.walletTopUpCommandHandler = walletTopUpCommandHandler;
        this.walletCreateCommandHandler = walletCreateCommandHandler;
        this.walletQueryService = walletQueryService;
    }

    @Override
    public WalletResponseDto createWallet(WalletCreateCommand walletCreateCommand) {
        log.info("creating wallet: for customer {}", walletCreateCommand.getCustomerId());
        return walletCreateCommandHandler.createWallet(walletCreateCommand);
    }

    @Override
    public WalletTopUpWithCardResponse cardWalletTopUp(WalletTopUpWithCardCommand walletTopUpWithCardCommand) {
        log.info("begin to top wallet for wallet tarcking id {}", walletTopUpWithCardCommand.getWalletTrackingId());
        return walletTopUpCommandHandler.topUpWalletWithCard(walletTopUpWithCardCommand);
    }

    @Override
    public WalletResponseDto findWalletByTrackingId(UUID trackingId) {
        return walletQueryService.getWalletByTrackingId(trackingId);

    }
}
