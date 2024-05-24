package com.playtomic.tests.wallet.wallet_domain_service;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.entity.valueobject.*;
import com.playtomic.tests.wallet.event.WalletTopUpEvent;
import com.playtomic.tests.wallet.event.WalletWithdrawEvent;
import com.playtomic.tests.wallet.exception.WalletDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
public class WalletDomainServiceImpl implements WalletDomainService {
    private static final String UTC = "UTC";

    @Override
    public WalletTopUpEvent topUpWallet(Wallet wallet, Money amount) {
        validateAmount(amount);
        addWalletBalance(wallet, amount);
        log.info("Wallet with tracking id: {} is topped up", wallet.getTrackingId().getId());
        return new WalletTopUpEvent(wallet, ZonedDateTime.now(ZoneId.of(UTC)), amount);

    }

    @Override
    public WalletWithdrawEvent withDraw(Wallet wallet, Money amount) {
        validateAmount(amount);
        deductWalletBalance(wallet, amount);
        log.info("Wallet with tracking id: {} is withdrawn up", wallet.getTrackingId().getId());
        return new WalletWithdrawEvent(wallet, ZonedDateTime.now(ZoneId.of(UTC)), amount);
    }

    @Override
    public Wallet InitializeWallet(Currency currency, WalletName name, CustomerId customerId) {
        if (name.getWalletName() == null) {
            name = new WalletName(currency.name());
        }

        return new Wallet(new Money(BigDecimal.ZERO, currency),
                name,
                new TrackingId(UUID.randomUUID()),
                customerId);

    }

    private void validateAmount(Money amount) {
        if (amount.isLessThanOrEqualZero()) {
            throw new WalletDomainException("Invalid amount");
        }
    }

    private void addWalletBalance(Wallet wallet, Money money) {
        wallet.setBalance(wallet.getBalance().add(money));
    }

    private void deductWalletBalance(Wallet wallet, Money money) {
        Money balance = wallet.getBalance().subtract(money);
        if (balance.isLessThanZero()) {
            throw new WalletDomainException("Withdrawn Amount exceeds the Balance");
        }
        wallet.setBalance(balance);
    }


}
