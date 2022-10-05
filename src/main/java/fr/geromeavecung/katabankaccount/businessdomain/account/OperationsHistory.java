package fr.geromeavecung.katabankaccount.businessdomain.account;

import java.util.ArrayList;
import java.util.List;

public class OperationsHistory {
    private final List<Deposit> deposits;

    public OperationsHistory() {
        this.deposits = new ArrayList<>();
    }

    public OperationsHistory(Deposit deposit) {
        this.deposits = new ArrayList<>();
        add(deposit);
    }

    public OperationsHistory(List<Deposit> deposits) {
        this.deposits = deposits;
    }

    public void add(Deposit deposit) {
        deposits.add(deposit);
    }
}
