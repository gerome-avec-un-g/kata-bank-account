package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Operation;

public record OperationView(String label, int operationSignedAmount, int balanceAfterOperation, String timestamp) {
    public OperationView(Operation operation) {
        this("TODO operation label!", operation.signedAmount(), 1, operation.timestamp().value().toString());
    }
}
