package com.playtomic.tests.wallet.exceptionhandler;

import com.playtomic.tests.wallet.exception.WalletDomainException;
import com.playtomic.tests.wallet.exception.WalletTopUpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WalletDomainException.class)
    public ResponseEntity<ErrorMessage> handleWalletDomainException(WalletDomainException ex) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WalletTopUpException.class)
    public ResponseEntity<ErrorMessage> handleWalletTopUpException(WalletTopUpException ex) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage(),
                ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGeneralException(Exception ex) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
