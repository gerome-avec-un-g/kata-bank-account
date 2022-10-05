package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;

public record DepositForm(int depositAmount) {

    public Amount toAmount() {
        return new Amount(depositAmount);
    }
}
