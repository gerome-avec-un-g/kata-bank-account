package fr.geromeavecung.katabankaccount.businessdomain;

public class Account {

    private final User owner;

    public Account(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }
}
