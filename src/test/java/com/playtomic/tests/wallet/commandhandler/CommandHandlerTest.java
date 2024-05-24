package com.playtomic.tests.wallet.commandhandler;


import com.playtomic.tests.wallet.command.WalletTopUpWithCardCommand;
import com.playtomic.tests.wallet.dto.Status;
import com.playtomic.tests.wallet.dto.WalletTopUpWithCardResponse;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.entity.valueobject.*;
import com.playtomic.tests.wallet.event.WalletTopUpEvent;
import com.playtomic.tests.wallet.exception.WalletDomainException;
import com.playtomic.tests.wallet.exception.WalletTopUpException;
import com.playtomic.tests.wallet.publisher.WalletTopRequestMessagePublisher;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.Payment;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;
import com.playtomic.tests.wallet.wallet_domain_service.WalletDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
public class CommandHandlerTest {

    @Mock
    private StripeService stripeService;

    @Mock
    private WalletTopRequestMessagePublisher walletTopRequestMessagePublisher;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletDomainService walletDomainService;

    @InjectMocks
    private WalletTopUpCommandHandler walletTopUpCommandHandler;

    private final UUID trackingId = UUID.randomUUID();
    private final Payment payment = new Payment("paymentId");
    private final Money money = new Money(BigDecimal.ZERO, Currency.USD);
    Wallet wallet = new Wallet(money, new WalletName("USD"), new TrackingId(trackingId), new CustomerId("customerId"));

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        walletTopUpCommandHandler = new WalletTopUpCommandHandler(
                stripeService,
                walletTopRequestMessagePublisher,
                walletRepository,
                walletDomainService
        );
    }

    @Test
    public void testTopUpWalletWithCard_Successful() throws StripeServiceException {
        // Arrange
        WalletTopUpWithCardCommand command = new WalletTopUpWithCardCommand("222222", BigDecimal.valueOf(100.00), Currency.USD, trackingId);
        when(stripeService.charge(any(), any())).thenReturn(payment);
        when(walletRepository.findByTrackingId(any(TrackingId.class))).thenReturn(Optional.of(wallet));
        when(walletDomainService.topUpWallet(any(), any())).thenReturn(new WalletTopUpEvent(wallet, ZonedDateTime.now(), money));

        // Act
        WalletTopUpWithCardResponse response = walletTopUpCommandHandler.topUpWalletWithCard(command);

        // Assert
        assertNotNull(response);
        assertEquals(trackingId, response.getWalletTrackingId());
        assertEquals(Status.SUCCESS, response.getStatus());
        assertEquals("Wallet Top Up was successful", response.getMessage());
        verify(walletTopRequestMessagePublisher, times(1)).publish(any());
    }

    @Test
    public void testTopUpWalletWithCard_WalletNotFound() {
        // Arrange
        WalletTopUpWithCardCommand command = new WalletTopUpWithCardCommand("222222", BigDecimal.valueOf(100.00), Currency.USD, trackingId);
        when(walletRepository.findByTrackingId(any())).thenReturn(Optional.empty());

        // Act & Assert
        WalletTopUpException exception = assertThrows(WalletTopUpException.class, () -> {
            walletTopUpCommandHandler.topUpWalletWithCard(command);
        });
        assertEquals("wallet cannot be found", exception.getMessage());
    }

    @Test
    public void testTopUpWalletWithCard_ChargingFailure() throws StripeServiceException {
        // Arrange
        WalletTopUpWithCardCommand command = new WalletTopUpWithCardCommand("222222", BigDecimal.valueOf(100.00), Currency.USD, trackingId);
        when(walletRepository.findByTrackingId(any(TrackingId.class))).thenReturn(Optional.of(wallet));
        when(stripeService.charge(any(), any())).thenThrow(new StripeServiceException());

        // Act & Assert
        WalletTopUpException exception = assertThrows(WalletTopUpException.class, () -> {
            walletTopUpCommandHandler.topUpWalletWithCard(command);
        });
        assertEquals("error occurred while charging card", exception.getMessage());
    }

    @Test
    public void testTopUpWalletWithCard_RetryLogic_Successful() throws StripeServiceException {
        // Arrange
        WalletTopUpWithCardCommand command = new WalletTopUpWithCardCommand("222222", BigDecimal.valueOf(100.00), Currency.USD, trackingId);
        Payment payment = new Payment("paymentId");
        when(stripeService.charge(any(), any())).thenReturn(payment);
        when(walletRepository.findByTrackingId(any(TrackingId.class))).thenReturn(Optional.of(wallet));
        when(walletDomainService.topUpWallet(any(), any())).thenThrow(new ObjectOptimisticLockingFailureException("Optimistic locking failed", null))
                .thenReturn(new WalletTopUpEvent(wallet, ZonedDateTime.now(), money));

        // Act
        WalletTopUpWithCardResponse response = walletTopUpCommandHandler.topUpWalletWithCard(command);

        // Assert
        assertNotNull(response);
        assertEquals(trackingId, response.getWalletTrackingId());
        assertEquals(Status.SUCCESS, response.getStatus());
        assertEquals("Wallet Top Up was successful", response.getMessage());
        verify(walletTopRequestMessagePublisher, times(1));
    }

    @Test
    public void testTopUpWalletWithCard_RetryLogic_UnSuccessful() throws StripeServiceException {
        // Arrange
        WalletTopUpWithCardCommand command = new WalletTopUpWithCardCommand("222222", BigDecimal.valueOf(100.00), Currency.USD, trackingId);
        Payment payment = new Payment("paymentId");
        when(stripeService.charge(any(), any())).thenReturn(payment);
        when(walletRepository.findByTrackingId(any(TrackingId.class))).thenReturn(Optional.of(wallet));
        when(walletDomainService.topUpWallet(any(), any())).thenThrow(new ObjectOptimisticLockingFailureException("Optimistic locking failed", null))
                .thenThrow(new ObjectOptimisticLockingFailureException("Optimistic locking failed", null))
                .thenThrow(new ObjectOptimisticLockingFailureException("Optimistic locking failed", null));


        // Act and Assert
        WalletDomainException exception = assertThrows(WalletDomainException.class, () -> {
            walletTopUpCommandHandler.topUpWalletWithCard(command);
        });
        assertEquals("Failed to execute action now, action will be processed later", exception.getMessage());
    }

}
