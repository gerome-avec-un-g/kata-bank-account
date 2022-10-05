package fr.geromeavecung.katabankaccount.businessdomain;

public record Deposit(Amount amount) {

    public Deposit(Amount amount) {
        if (amount.value() < 0) {//TODO also forbid 0
            throw new IllegalArgumentException("Deposit amount can't be less or equal to 0");
        }
        this.amount = amount;
    }
}
