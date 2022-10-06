package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.ReadAccount;
import fr.geromeavecung.katabankaccount.businessdomain.account.Timestamps;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;

public class AUserDisplaysHisOperationHistoryAndAccountBalance {
    private final ReadAccount readAccount;
    private final Timestamps timestamps;

    public AUserDisplaysHisOperationHistoryAndAccountBalance(ReadAccount readAccount, Timestamps timestamps) {
        this.readAccount = readAccount;
        this.timestamps = timestamps;
    }

    public AccountView execute(User user) {
        return new AccountView(readAccount.forUser(user));
    }
}
