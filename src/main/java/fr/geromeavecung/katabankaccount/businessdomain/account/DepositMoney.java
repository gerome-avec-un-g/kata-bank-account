package fr.geromeavecung.katabankaccount.businessdomain.account;

import fr.geromeavecung.katabankaccount.businessdomain.core.Timestamps;
import fr.geromeavecung.katabankaccount.businessdomain.core.ConnectedUser;

public class DepositMoney {

    private final Accounts accounts;

    private final Timestamps timestamps;

    public DepositMoney(Accounts accounts, Timestamps timestamps) {
        this.accounts = accounts;
        this.timestamps = timestamps;
    }

    public void execute(ConnectedUser user, Amount amount) {
        Account account = accounts.forUser(user).orElseThrow(() -> new IllegalStateException("user " + user.uuid() + " has no account"));
        account.deposit(amount, timestamps.now());
    }
}
