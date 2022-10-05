package fr.geromeavecung.katabankaccount.businessdomain;

public class CreateAccount {

    private final Accounts accounts;

    public CreateAccount(Accounts accounts) {
        this.accounts = accounts;
    }

    public void execute(User user, Amount amount) {
        accounts.save(createAccount(user, amount));
    }

    private static Account createAccount(User user, Amount amount) {
        if (amount.value() == 0) {
            return new Account(user);
        }
        return new Account(user, new OperationsHistory(new Deposit(amount)));
    }
}