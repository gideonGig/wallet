package com.playtomic.tests.wallet.wallet_domain_service;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.entity.valueobject.Currency;
import com.playtomic.tests.wallet.entity.valueobject.CustomerId;
import com.playtomic.tests.wallet.entity.valueobject.Money;
import com.playtomic.tests.wallet.entity.valueobject.WalletName;
import com.playtomic.tests.wallet.event.WalletTopUpEvent;
import com.playtomic.tests.wallet.event.WalletWithdrawEvent;

public interface WalletDomainService {
    WalletTopUpEvent topUpWallet(Wallet wallet, Money amount);

    WalletWithdrawEvent withDraw(Wallet wallet, Money amount);

    Wallet InitializeWallet(Currency currency, WalletName name, CustomerId customerId);
}
