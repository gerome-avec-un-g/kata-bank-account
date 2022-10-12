package fr.geromeavecung.katabankaccount.usecases.customer.createsaccount;

import fr.geromeavecung.katabankaccount.businessdomain.account.CreateAccount;
import fr.geromeavecung.katabankaccount.businessdomain.core.ConnectedUser;

public class ACustomerCreatesABankAccount {
    private final CreateAccount createAccount;

    public ACustomerCreatesABankAccount(CreateAccount createAccount) {
        this.createAccount = createAccount;
    }

    public void execute(ConnectedUser connectedUser, AccountCreationRequest accountCreationRequest) {
        createAccount.execute(connectedUser, accountCreationRequest.toAmount());
    }
}
