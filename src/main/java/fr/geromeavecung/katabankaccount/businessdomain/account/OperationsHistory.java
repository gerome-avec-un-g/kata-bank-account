package fr.geromeavecung.katabankaccount.businessdomain.account;

import java.util.ArrayList;
import java.util.List;

public class OperationsHistory {
    private final List<Operation> operations;

    public OperationsHistory() {
        this.operations = new ArrayList<>();
    }

    public OperationsHistory(Operation operation) {
        this.operations = new ArrayList<>();
        add(operation);
    }

    public OperationsHistory(List<Operation> operations) {
        // new ArrayList because parameter can be an immutable list
        this.operations = new ArrayList<>(operations);
    }

    public void add(Operation operation) {
        operations.add(operation);
    }
}
