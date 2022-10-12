package fr.geromeavecung.katabankaccount.businessdomain.account;

import fr.geromeavecung.katabankaccount.businessdomain.core.Timestamps;
import fr.geromeavecung.katabankaccount.businessdomain.core.ConnectedUser;

public class CreateAccount {

    private final Accounts accounts;

    private final Timestamps timestamps;

    public CreateAccount(Accounts accounts, Timestamps timestamps) {
        this.accounts = accounts;
        this.timestamps = timestamps;
    }

    public void execute(ConnectedUser user, Amount amount) {
        if (accounts.forUser(user).isPresent()) {
            throw new IllegalStateException("a user can't have two accounts");
        }
        accounts.save(Account.create(user, amount, timestamps.now()));
    }

}
