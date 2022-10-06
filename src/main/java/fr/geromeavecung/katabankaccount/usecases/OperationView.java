package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Operation;

public record OperationView(String label, int operationSignedAmount, int balanceAfterOperation, String timestamp) {
    public OperationView(Operation operation, int previousBalance) {
        this("TODO operation label!", operation.signedAmount(), previousBalance + operation.signedAmount(), operation.timestamp().value().toString());
    }
}
