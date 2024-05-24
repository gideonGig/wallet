package com.playtomic.tests.wallet.exception;

import org.springframework.http.HttpStatus;

public class WalletTopUpException extends RuntimeException {
    private final HttpStatus status;

    public WalletTopUpException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public WalletTopUpException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
