package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.Accounts;
import fr.geromeavecung.katabankaccount.businessdomain.CreateAccount;
import fr.geromeavecung.katabankaccount.businessdomain.User;

public class AUserCreateABankAccount {
    private CreateAccount createAccount;

    public AUserCreateABankAccount(CreateAccount createAccount) {
        this.createAccount = createAccount;
    }

    public void execute(User user) {
        createAccount.execute(user);
    }
}
