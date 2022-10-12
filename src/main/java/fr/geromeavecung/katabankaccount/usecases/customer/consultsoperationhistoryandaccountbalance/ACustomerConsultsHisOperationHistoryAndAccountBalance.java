package fr.geromeavecung.katabankaccount.usecases.customer.consultsoperationhistoryandaccountbalance;

import fr.geromeavecung.katabankaccount.businessdomain.account.ReadAccount;
import fr.geromeavecung.katabankaccount.businessdomain.core.ConnectedUser;

public class ACustomerConsultsHisOperationHistoryAndAccountBalance {
    private final ReadAccount readAccount;

    public ACustomerConsultsHisOperationHistoryAndAccountBalance(ReadAccount readAccount) {
        this.readAccount = readAccount;
    }

    public AccountView execute(ConnectedUser user) {
        return new AccountView(readAccount.forUser(user));
    }
}
