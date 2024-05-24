package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.entity.valueobject.TrackingId;
import com.playtomic.tests.wallet.entity.valueobject.WalletId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, WalletId> {

    @Query("select w from Wallet w where w.trackingId = :trackingId")
    Optional<Wallet> findByTrackingId(TrackingId trackingId);
}
