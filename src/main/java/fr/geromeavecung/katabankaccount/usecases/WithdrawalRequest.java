package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;

public record WithdrawalRequest(int amount) {
    public Amount toAmount() {
        return new Amount(amount);
    }
}
