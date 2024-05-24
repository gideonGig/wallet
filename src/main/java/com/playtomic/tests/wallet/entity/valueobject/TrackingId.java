package com.playtomic.tests.wallet.entity.valueobject;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class TrackingId implements Serializable {
    private UUID trackingId;

    protected TrackingId() {
        // Required by JPA
    }

    public TrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }

    public UUID getId() {
        return trackingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackingId trackingId = (TrackingId) o;
        return Objects.equals(trackingId, trackingId.trackingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackingId);
    }
}
