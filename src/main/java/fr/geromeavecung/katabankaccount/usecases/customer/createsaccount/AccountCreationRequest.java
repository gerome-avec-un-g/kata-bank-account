package fr.geromeavecung.katabankaccount.usecases.customer.createsaccount;

import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;

public record AccountCreationRequest(int firstDepositAmount) {

    public Amount toAmount() {
        return new Amount(firstDepositAmount);
    }
}
