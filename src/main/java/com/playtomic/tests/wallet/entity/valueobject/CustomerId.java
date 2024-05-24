package com.playtomic.tests.wallet.entity.valueobject;

import java.io.Serializable;
import java.util.Objects;
public class CustomerId implements Serializable {
    private String customerId;

    public CustomerId(String customerId) {
        this.customerId = customerId;
    }

    protected CustomerId() {
        // Required by JPA
    }

    public String getId() {
        return customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerId trackingId = (CustomerId) o;
        return Objects.equals(trackingId, trackingId.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

}
