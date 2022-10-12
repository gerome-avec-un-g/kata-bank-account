package fr.geromeavecung.katabankaccount.businessdomain.account;

import fr.geromeavecung.katabankaccount.businessdomain.core.Timestamp;
import fr.geromeavecung.katabankaccount.businessdomain.core.ConnectedUser;

import java.util.List;

public class Account {

    private static final int MINIMUM_BALANCE = -100;
    private final ConnectedUser owner;
    private final OperationsHistory operationsHistory;


    public static Account create(ConnectedUser user, Amount amount, Timestamp timestamp) {
        if (amount.value() == 0) {
            return new Account(user);
        }
        return new Account(user, new OperationsHistory(new Deposit(amount, timestamp)));
    }

    public Account(ConnectedUser owner) {
        this.owner = owner;
        this.operationsHistory = new OperationsHistory();
    }

    public Account(ConnectedUser owner, OperationsHistory operationsHistory) {
        this.owner = owner;
        this.operationsHistory = operationsHistory;
    }

    public ConnectedUser getOwner() {
        return owner;
    }

    public void deposit(Amount amount, Timestamp timestamp) {
        operationsHistory.add(new Deposit(amount, timestamp));
    }

    public void withdraw(Amount amount, Timestamp timestamp) {
        Withdrawal withdrawal = new Withdrawal(amount, timestamp);
        if (operationsHistory.expectedBalance(withdrawal) < MINIMUM_BALANCE) {
            throw new IllegalStateException("balance of account can't be below " + MINIMUM_BALANCE + ", was: -101");
        }
        operationsHistory.add(withdrawal);
    }

    public List<Operation> getOperations() {
        return operationsHistory.getOperations();
    }

    public int balance() {
        return operationsHistory.balance();
    }
}
