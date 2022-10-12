package fr.geromeavecung.katabankaccount.businessdomain.account;

import fr.geromeavecung.katabankaccount.businessdomain.core.Timestamp;
import fr.geromeavecung.katabankaccount.businessdomain.core.ConnectedUser;

public class WithdrawMoney {
    private final Accounts accounts;

    public WithdrawMoney(Accounts accounts) {
        this.accounts = accounts;
    }

    public void execute(ConnectedUser user, Amount amount, Timestamp timestamp) {
        Account account = accounts.forUser(user).orElseThrow(() -> new IllegalStateException("user " + user.uuid() + " has no account"));
        account.withdraw(amount, timestamp);
    }

}
