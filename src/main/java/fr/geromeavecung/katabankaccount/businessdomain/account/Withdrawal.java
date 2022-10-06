package fr.geromeavecung.katabankaccount.businessdomain.account;

public record Withdrawal(Amount amount) implements Operation {

    public Withdrawal {
        if (amount.value() <= 0) {
            throw new IllegalArgumentException("Withdrawal amount can't be less or equal to 0, was: " + amount.value());
        }
    }

}
