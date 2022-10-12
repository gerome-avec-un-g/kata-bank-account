package fr.geromeavecung.katabankaccount.businessdomain.account;

import fr.geromeavecung.katabankaccount.businessdomain.core.Timestamp;

public record Deposit(Amount amount, Timestamp timestamp) implements Operation {

    private static final int MINIMUM_AMOUNT = 0;

    public Deposit {
        if (amount.value() <= MINIMUM_AMOUNT) {
            throw new IllegalArgumentException("Deposit amount can't be less or equal to 0, was: " + amount.value());
        }
    }

    @Override
    public int signedAmount() {
        return amount.value();
    }
}
