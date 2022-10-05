package fr.geromeavecung.katabankaccount.businessdomain;

public class Account {

    private final User owner;
    private final OperationsHistory operationsHistory;

    public Account(User owner) {
        this.owner = owner;
        this.operationsHistory = new OperationsHistory();
    }

    public Account(User owner, OperationsHistory operationsHistory) {
        this.owner = owner;
        this.operationsHistory = operationsHistory;
    }

    public User getOwner() {
        return owner;
    }
}