package fr.geromeavecung.katabankaccount.businessdomain.account;

public class DepositMoney {

    private final Accounts accounts;

    private final Timestamps timestamps;

    public DepositMoney(Accounts accounts, Timestamps timestamps) {
        this.accounts = accounts;
        this.timestamps = timestamps;
    }

    public void execute(User user, Amount amount) {
        Account account = accounts.forUser(user).orElseThrow(() -> new IllegalStateException("user " + user.uuid() + " has no account"));
        account.deposit(amount, timestamps.now());
    }
}
