package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.CreateAccount;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;

public class AUserCreateABankAccount {
    private final CreateAccount createAccount;

    public AUserCreateABankAccount(CreateAccount createAccount) {
        this.createAccount = createAccount;
    }

    public void execute(User connectedUser, AccountCreationForm accountCreationForm) {
        createAccount.execute(connectedUser, accountCreationForm.toAmount());
    }
}
