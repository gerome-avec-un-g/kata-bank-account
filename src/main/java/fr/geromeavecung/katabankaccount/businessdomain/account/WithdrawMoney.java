package fr.geromeavecung.katabankaccount.businessdomain.account;

public class WithdrawMoney {
    private final Accounts accounts;

    public WithdrawMoney(Accounts accounts) {
        this.accounts = accounts;
    }

    public void execute(User user, Amount amount, Timestamp timestamp) {
        Account account = accounts.forUser(user).orElseThrow(() -> new IllegalStateException("user " + user.uuid() + " has no account"));
        account.withdraw(amount, timestamp);
    }

}
