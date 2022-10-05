package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Account;
import fr.geromeavecung.katabankaccount.businessdomain.account.Accounts;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountsInMemory implements Accounts {

    private final Map<User, Account> accountsByUser = new HashMap<>();

    @Override
    public void save(Account account) {
        accountsByUser.put(account.getOwner(), account);
    }

    @Override
    public Optional<Account> forUser(User user) {
        return Optional.ofNullable(accountsByUser.get(user));
    }
}
