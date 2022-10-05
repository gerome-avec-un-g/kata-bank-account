package fr.geromeavecung.katabankaccount.businessdomain;

import java.util.Optional;

public interface Accounts {


    void save(Account account);

    Optional<Account> forUser(User user);
}
