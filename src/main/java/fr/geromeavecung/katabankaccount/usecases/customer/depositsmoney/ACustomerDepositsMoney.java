package fr.geromeavecung.katabankaccount.usecases.customer.depositsmoney;

import fr.geromeavecung.katabankaccount.businessdomain.account.DepositMoney;
import fr.geromeavecung.katabankaccount.businessdomain.core.ConnectedUser;

public class ACustomerDepositsMoney {

    private final DepositMoney depositMoney;

    public ACustomerDepositsMoney(DepositMoney depositMoney) {
        this.depositMoney = depositMoney;
    }

    public void execute(ConnectedUser user, DepositRequest depositRequest) {
        depositMoney.execute(user, depositRequest.toAmount());
    }
}
