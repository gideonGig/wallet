package com.playtomic.tests.wallet.entity.base;

import java.util.Objects;

public abstract class BaseEntity<ID> {

    protected BaseEntity() {

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public abstract ID getId();
}
