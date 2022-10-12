package fr.geromeavecung.katabankaccount.usecases.customer.depositsmoney;

import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;

public record DepositRequest(int depositAmount) {

    public Amount toAmount() {
        return new Amount(depositAmount);
    }
}
