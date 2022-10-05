package fr.geromeavecung.katabankaccount.businessdomain;

public class CreateAccount {

    private final Accounts accounts;

    public CreateAccount(Accounts accounts) {
        this.accounts = accounts;
    }

    public void execute(User user) {
        Account account = new Account(user);
        accounts.save(account);
    }
}
