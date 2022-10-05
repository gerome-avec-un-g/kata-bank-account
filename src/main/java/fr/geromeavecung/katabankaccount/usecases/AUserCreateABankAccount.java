package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.CreateAccount;
import fr.geromeavecung.katabankaccount.businessdomain.User;

public class AUserCreateABankAccount {
    private final CreateAccount createAccount;

    public AUserCreateABankAccount(CreateAccount createAccount) {
        this.createAccount = createAccount;
    }

    public void execute(User user, AccountCreationForm accountCreationForm) {
        createAccount.execute(user, accountCreationForm.toAmount());
    }
}
