package com.playtomic.tests.wallet.commandhandler;

import com.playtomic.tests.wallet.command.WalletCreateCommand;
import com.playtomic.tests.wallet.dto.WalletResponseDto;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.entity.valueobject.CustomerId;
import com.playtomic.tests.wallet.entity.valueobject.WalletName;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.wallet_domain_service.WalletDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WalletCreateCommandHandler {
    private final WalletDomainService walletDomainService;
    private final WalletRepository walletRepository;

    public WalletCreateCommandHandler(WalletDomainService walletDomainService, WalletRepository walletRepository) {
        this.walletDomainService = walletDomainService;
        this.walletRepository = walletRepository;
    }

    public WalletResponseDto createWallet(WalletCreateCommand walletCreateCommand) {
        /**
         * you can decide to publish a wallet create event here..
         * to a Transacion Service, that appends every event that does
         * a topup or debit of a wallet
         */
        Wallet wallet = walletDomainService.InitializeWallet(walletCreateCommand.getCurrency(),
                new WalletName(walletCreateCommand.getWalletName()),
                new CustomerId(walletCreateCommand.getCustomerId()));
        walletRepository.save(wallet);
        return WalletResponseDto.builder().walletTrackingId(wallet.getTrackingId().getId())
                .amount(wallet.getBalance().getAmount())
                .Currency(wallet.getBalance().getCurrency().name())
                .build();
    }
}
