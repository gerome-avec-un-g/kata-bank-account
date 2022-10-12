package fr.geromeavecung.katabankaccount.businessdomain.account;

import fr.geromeavecung.katabankaccount.businessdomain.core.ConnectedUser;

import java.util.Optional;

public interface Accounts {


    void save(Account account);

    Optional<Account> forUser(ConnectedUser user);
}
