package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.ReadAccount;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;

public class AUserDisplaysHisOperationHistoryAndAccountBalance {
    private final ReadAccount readAccount;

    public AUserDisplaysHisOperationHistoryAndAccountBalance(ReadAccount readAccount) {
        this.readAccount = readAccount;
    }

    public AccountView execute(User user) {
        return new AccountView(readAccount.forUser(user));
    }
}
