package com.playtomic.tests.wallet.entity.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
    private BigDecimal amount;
    private Currency currency;

    public static final Money ZERO_USD = new Money(BigDecimal.ZERO, Currency.USD);

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("Amount and currency must not be null");
        }
        this.amount = setScale(amount);
        this.currency = currency;
    }


    public boolean isLessThanOrEqualZero() {
        return this.amount.compareTo(BigDecimal.ZERO) <= 0;
    }

    public boolean isLessThanZero() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }


    public boolean isGreaterThan(Money money) {
        validateCurrencyMatch(money);
        return this.amount.compareTo(money.getAmount()) > 0;
    }

    public boolean isLessThan(Money money) {
        validateCurrencyMatch(money);
        return this.amount.compareTo(money.getAmount()) < 0;
    }


    public Money add(Money money) {
        validateCurrencyMatch(money);
        return new Money(setScale(this.amount.add(money.getAmount())), this.currency);
    }

    public Money subtract(Money money) {
        validateCurrencyMatch(money);
        return new Money(setScale(this.amount.subtract(money.getAmount())), this.currency);
    }

    public Money multiply(int multiplier) {
        return new Money(setScale(this.amount.multiply(new BigDecimal(multiplier))), this.currency);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.equals(money.amount) && currency == money.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    private BigDecimal setScale(BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }

    private void validateCurrencyMatch(Money money) {
        if (this.currency != money.getCurrency()) {
            throw new IllegalArgumentException("Currencies must match");
        }
    }

    protected Money() {

    }

}
