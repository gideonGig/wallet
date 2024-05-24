package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.application.WalletService;
import com.playtomic.tests.wallet.command.WalletCreateCommand;
import com.playtomic.tests.wallet.command.WalletTopUpWithCardCommand;
import com.playtomic.tests.wallet.dto.WalletResponseDto;
import com.playtomic.tests.wallet.dto.WalletTopUpWithCardResponse;
import com.playtomic.tests.wallet.entity.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wallets")
@Validated
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);
    private final WalletService walletService;

    @RequestMapping("/log")
    public void log() {

        log.info("Logging from /");
    }

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/createWallet")
    public ResponseEntity<WalletResponseDto> createWallet(@Validated @RequestBody WalletCreateCommand createCommand) {
        WalletResponseDto wallet = walletService.createWallet(createCommand);
        return new ResponseEntity<>(wallet, HttpStatus.CREATED);
    }

    @PostMapping("/topUpWithCard")
    public ResponseEntity<WalletTopUpWithCardResponse> topUpWalletWithCard(@Validated @RequestBody WalletTopUpWithCardCommand walletTopUpWithCardCommand) {
        WalletTopUpWithCardResponse walletResponse = walletService.cardWalletTopUp(walletTopUpWithCardCommand);
        return new ResponseEntity<>(walletResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<WalletResponseDto> getbyTrackingId(@RequestParam UUID trackingId) {
        WalletResponseDto walletResponse = walletService.findWalletByTrackingId(trackingId);
        return new ResponseEntity<>(walletResponse, HttpStatus.OK);
    }
}
