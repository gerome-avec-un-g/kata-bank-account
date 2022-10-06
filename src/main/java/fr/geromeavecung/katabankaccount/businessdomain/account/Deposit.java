package fr.geromeavecung.katabankaccount.businessdomain.account;

public record Deposit(Amount amount) {

    public Deposit(Amount amount) {
        if (amount.value() <= 0) {
            throw new IllegalArgumentException("Deposit amount can't be less or equal to 0, was: " + amount.value());
        }
        this.amount = amount;
    }
}
