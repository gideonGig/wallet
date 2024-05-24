package com.playtomic.tests.wallet.query;


import com.playtomic.tests.wallet.dto.WalletResponseDto;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.entity.valueobject.TrackingId;
import com.playtomic.tests.wallet.exception.WalletDomainException;
import com.playtomic.tests.wallet.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class WalletQueryService {
    private final WalletRepository walletRepository;

    public WalletQueryService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public WalletResponseDto getWalletByTrackingId(UUID trackingId) {
        Optional<Wallet> optionalWallet = walletRepository.findByTrackingId(new TrackingId(trackingId));
        if (optionalWallet.isEmpty()) {
            throw new WalletDomainException("wallet with tracking Id " + trackingId + ", cannot be found");
        }

        Wallet wallet = optionalWallet.get();
        return WalletResponseDto.builder().walletTrackingId(wallet.getTrackingId().getId())
                .amount(wallet.getBalance().getAmount())
                .Currency(wallet.getBalance().getCurrency().name())
                .build();
    }

}
