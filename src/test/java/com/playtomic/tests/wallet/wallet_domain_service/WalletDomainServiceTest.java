package com.playtomic.tests.wallet.wallet_domain_service;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.entity.valueobject.Currency;
import com.playtomic.tests.wallet.entity.valueobject.CustomerId;
import com.playtomic.tests.wallet.entity.valueobject.Money;
import com.playtomic.tests.wallet.entity.valueobject.WalletName;
import com.playtomic.tests.wallet.event.WalletTopUpEvent;
import com.playtomic.tests.wallet.event.WalletWithdrawEvent;
import com.playtomic.tests.wallet.exception.WalletDomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class WalletDomainServiceTest {
    @InjectMocks
    private WalletDomainServiceImpl walletDomainService;

    private Wallet wallet;
    private WalletName walletName;
    private CustomerId customerId;

    @BeforeEach
    void setUp() {
        walletName = new WalletName("USD");
        customerId = new CustomerId("customer_id");
        walletDomainService = new WalletDomainServiceImpl();
        wallet = walletDomainService.InitializeWallet(Currency.USD, walletName, customerId);
    }

    @Test
    public void topUpWallet() {
        Money amount = new Money(new BigDecimal("100.00"), Currency.USD);
        WalletTopUpEvent event = walletDomainService.topUpWallet(wallet, amount);
        assertNotNull(event);
        assertEquals(new BigDecimal("100.00"), wallet.getBalance().getAmount());
    }

    @Test
    public void withDraw() {
        Money amount = new Money(new BigDecimal("200.00"), Currency.USD);
        walletDomainService.topUpWallet(wallet, amount);
        Money withDrawnAmount = new Money(new BigDecimal("200.00"), Currency.USD);
        WalletWithdrawEvent event = walletDomainService.withDraw(wallet, withDrawnAmount);

        assertNotNull(event);
        assertEquals(new BigDecimal("0.00"), wallet.getBalance().getAmount());
    }

    @Test
    public void initializeWallet() {
        assertNotNull(wallet);
        assertEquals(customerId, wallet.getCustomerId());
        assertEquals(new BigDecimal("0.00"), wallet.getBalance().getAmount());
        assertNotNull(wallet.getTrackingId().getId());
    }

    @Test
    public void topUpWallet_withInvalidAmount() {
        Money invalidAmount = new Money(BigDecimal.ZERO, Currency.USD);

        WalletDomainException exception = assertThrows(WalletDomainException.class, () -> {
            walletDomainService.topUpWallet(wallet, invalidAmount);
        });

        assertEquals("Invalid amount", exception.getMessage());
    }

    @Test
    public void withDraw_withInvalidAmount() {
        Money invalidAmount = new Money(BigDecimal.ZERO, Currency.USD);

        WalletDomainException exception = assertThrows(WalletDomainException.class, () -> {
            walletDomainService.withDraw(wallet, invalidAmount);
        });

        assertEquals("Invalid amount", exception.getMessage());
    }

    @Test
    public void withDraw_exceedsBalance() {
        Money amount = new Money(new BigDecimal("100.00"), Currency.USD);
        walletDomainService.topUpWallet(wallet, amount);
        Money withDrawnAmount = new Money(new BigDecimal("200.00"), Currency.USD);

        // Act & Assert
        WalletDomainException exception = assertThrows(WalletDomainException.class, () -> {
            WalletWithdrawEvent event = walletDomainService.withDraw(wallet, withDrawnAmount);
        });
        assertEquals("Withdrawn Amount exceeds the Balance", exception.getMessage());
    }
}
