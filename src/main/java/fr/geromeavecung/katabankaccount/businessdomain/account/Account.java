package fr.geromeavecung.katabankaccount.businessdomain.account;

public class Account {

    private final User owner;
    private final OperationsHistory operationsHistory;


    public static Account create(User user, Amount amount, Timestamp timestamp) {
        if (amount.value() == 0) {
            return new Account(user);
        }
        return new Account(user, new OperationsHistory(new Deposit(amount, timestamp)));
    }
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

    public void deposit(Amount amount, Timestamp timestamp) {
        operationsHistory.add(new Deposit(amount, timestamp));
    }

    public void withdraw(Amount amount, Timestamp timestamp) {
        operationsHistory.add(new Withdrawal(amount, timestamp));
    }
}
