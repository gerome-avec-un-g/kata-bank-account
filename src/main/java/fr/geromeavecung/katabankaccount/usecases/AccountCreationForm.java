package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.Amount;

public record AccountCreationForm(int firstDepositAmount) {

    public Amount toAmount() {
        return new Amount(firstDepositAmount);
    }
}