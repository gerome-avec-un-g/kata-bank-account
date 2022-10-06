package fr.geromeavecung.katabankaccount.businessdomain.account;

public record Amount(int value) {
    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
