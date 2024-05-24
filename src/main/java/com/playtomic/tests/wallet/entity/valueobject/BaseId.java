package com.playtomic.tests.wallet.entity.valueobject;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public abstract class BaseId<T> {
    private  T value;

    protected  BaseId() {

    }

    protected BaseId(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseId<?> baseId = (BaseId<?>) o;
        return value.equals(baseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
