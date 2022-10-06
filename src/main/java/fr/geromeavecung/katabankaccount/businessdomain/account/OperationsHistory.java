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
        // new ArrayList because parameter can be an immutable list
        this.deposits = new ArrayList<>(deposits);
    }

    public void add(Deposit deposit) {
        deposits.add(deposit);
    }
}
