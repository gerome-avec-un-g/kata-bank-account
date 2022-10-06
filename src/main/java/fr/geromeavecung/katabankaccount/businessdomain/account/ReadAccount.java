package fr.geromeavecung.katabankaccount.businessdomain.account;

public class ReadAccount {
    private final Accounts accounts;

    public ReadAccount(Accounts accounts) {
        this.accounts = accounts;
    }

    public Account forUser(User user) {
        return accounts.forUser(user).get(); // FIXME user without account
    }
}
