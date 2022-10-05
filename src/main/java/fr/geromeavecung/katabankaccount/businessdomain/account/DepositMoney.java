package fr.geromeavecung.katabankaccount.businessdomain.account;

public class DepositMoney {

    private final Accounts accounts;

    public DepositMoney(Accounts accounts) {
        this.accounts = accounts;
    }

    public void execute(User user, Amount amount) {
        Account account = accounts.forUser(user).orElseThrow(() -> new IllegalStateException("user has no account"));
        account.deposit(amount);
    }
}
