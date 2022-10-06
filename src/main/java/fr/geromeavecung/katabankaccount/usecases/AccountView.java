package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Account;
import fr.geromeavecung.katabankaccount.businessdomain.account.Operation;

import java.util.ArrayList;
import java.util.List;

public record AccountView(List<OperationView> operations) {

    public AccountView(Account account) {
        this(convertOperationViews(account));
    }

    private static List<OperationView> convertOperationViews(Account account) {
        int balance = 0;
        List<OperationView> operationViews = new ArrayList<>();
        for (Operation operation : account.getOperations()) {
            OperationView operationView = new OperationView(operation, balance);
            operationViews.add(operationView);
            balance = operationView.balanceAfterOperation();
        }
        return operationViews;
    }

}
