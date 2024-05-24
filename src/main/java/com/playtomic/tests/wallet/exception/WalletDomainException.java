package com.playtomic.tests.wallet.exception;

import org.springframework.http.HttpStatus;

public class WalletDomainException extends RuntimeException{
    private final HttpStatus status;

    public WalletDomainException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public WalletDomainException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
