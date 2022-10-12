package fr.geromeavecung.katabankaccount.usecases.customer.withdrawsmoney;

import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;

public record WithdrawalRequest(int amount) {
    public Amount toAmount() {
        return new Amount(amount);
    }
}
