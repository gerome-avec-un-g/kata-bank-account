package fr.geromeavecung.katabankaccount.businessdomain.account;

import fr.geromeavecung.katabankaccount.businessdomain.core.ConnectedUser;

public class ReadAccount {
    private final Accounts accounts;

    public ReadAccount(Accounts accounts) {
        this.accounts = accounts;
    }

    public Account forUser(ConnectedUser user) {
        return accounts.forUser(user).orElseThrow(() -> new IllegalStateException("user " + user.uuid() + " has no account"));
    }
}
