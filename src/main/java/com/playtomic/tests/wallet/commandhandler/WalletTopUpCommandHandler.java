package com.playtomic.tests.wallet.commandhandler;

import com.playtomic.tests.wallet.command.WalletTopUpWithCardCommand;
import com.playtomic.tests.wallet.dto.Status;
import com.playtomic.tests.wallet.dto.WalletTopUpWithCardResponse;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.entity.valueobject.Money;
import com.playtomic.tests.wallet.entity.valueobject.TrackingId;
import com.playtomic.tests.wallet.entity.valueobject.WalletId;
import com.playtomic.tests.wallet.event.WalletTopUpEvent;
import com.playtomic.tests.wallet.exception.WalletDomainException;
import com.playtomic.tests.wallet.exception.WalletTopUpException;
import com.playtomic.tests.wallet.publisher.WalletTopRequestMessagePublisher;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.Payment;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;
import com.playtomic.tests.wallet.wallet_domain_service.WalletDomainService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Component
public class WalletTopUpCommandHandler {
    private final StripeService stripService;

    private final WalletTopRequestMessagePublisher walletTopRequestMessagePublisher;

    private final WalletRepository walletRepository;

    private final WalletDomainService walletDomainService;

    public WalletTopUpCommandHandler(StripeService stripService,
                                     WalletTopRequestMessagePublisher walletTopRequestMessagePublisher,
                                     WalletRepository walletRepository,
                                     WalletDomainService walletDomainService) {

        this.stripService = stripService;
        this.walletTopRequestMessagePublisher = walletTopRequestMessagePublisher;
        this.walletRepository = walletRepository;
        this.walletDomainService = walletDomainService;
    }

    public WalletTopUpWithCardResponse topUpWalletWithCard(WalletTopUpWithCardCommand walletTopUpWithCardCommand) {
        Optional<Wallet> optionalWallet = walletRepository.findByTrackingId(new TrackingId(walletTopUpWithCardCommand.getWalletTrackingId()));
        if (optionalWallet.isEmpty()) {
            throw new WalletTopUpException("wallet cannot be found", HttpStatus.NOT_FOUND);
        }

        //retry logic can be implemeneted here where there is network failure, timeout etc, Strip api should provide Idempotency
        //key can be used with the retry logic with exponetial backoff and circuit breaker

        try {
            stripService.charge(walletTopUpWithCardCommand.getCardNumber(), walletTopUpWithCardCommand.getAmount());
        } catch (StripeServiceException exception) {
            log.error("error occurred while charging card: {}", exception.getMessage());
            throw new WalletTopUpException("error occurred while charging card", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        //we can log every request call and response for the strip service here
        Wallet wallet = optionalWallet.get();
        WalletTopUpEvent walletTopUpEvent = topUpWalletBalance(wallet, walletTopUpWithCardCommand);
        walletTopRequestMessagePublisher.publish(walletTopUpEvent);
        return new WalletTopUpWithCardResponse(wallet.getTrackingId().getId(), Status.SUCCESS,
                "Wallet Top Up was successful");

    }

    @Transactional
    private WalletTopUpEvent topUpWalletBalance(Wallet wallet, WalletTopUpWithCardCommand walletTopUpWithCardCommand) {
        return executeWithRetry(() -> {
            WalletTopUpEvent walletTopUpEvent = walletDomainService.topUpWallet(wallet, new Money(walletTopUpWithCardCommand.getAmount(),
                    walletTopUpWithCardCommand.getCurrency()));
            walletRepository.save(wallet);
            log.info("wallet with trackingId {} is saved after top up", wallet.getTrackingId());
            return walletTopUpEvent;
        });
    }

    private WalletTopUpEvent executeWithRetry(Supplier<WalletTopUpEvent> action) {
        int MAX_RETRY_COUNT = 3;
        int attempt = 0;
        boolean success = false;

        while (attempt < MAX_RETRY_COUNT && !success) {
            try {
                WalletTopUpEvent event = action.get();
                success = true;
                return event;
            } catch (ObjectOptimisticLockingFailureException e) {
                attempt++;
                if (attempt >= MAX_RETRY_COUNT) {
                    //log the failure after multiple attempts, send a highly priority message to
                    //team for manual intervention
                    throw new WalletDomainException("Failed to execute action now, action will be processed later", HttpStatus.PROCESSING);
                }
            }
        }

        throw new WalletDomainException("Failed to execute action now, action will be processed later", HttpStatus.PROCESSING);

    }


}
