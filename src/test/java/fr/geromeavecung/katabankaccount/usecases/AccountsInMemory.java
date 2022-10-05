package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.Account;
import fr.geromeavecung.katabankaccount.businessdomain.Accounts;
import fr.geromeavecung.katabankaccount.businessdomain.User;

import java.util.HashMap;
import java.util.Map;

public class AccountsInMemory implements Accounts {

    private final Map<User, Account> accountsByUser = new HashMap<>();

    public Map<User, Account> getAccountsByUser() {
        return accountsByUser;
    }

    @Override
    public void save(Account account) {
        accountsByUser.put(account.getOwner(), account);
    }
}
