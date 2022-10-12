package fr.geromeavecung.katabankaccount.usecases.customer.withdrawsmoney;

import fr.geromeavecung.katabankaccount.businessdomain.core.Timestamps;
import fr.geromeavecung.katabankaccount.businessdomain.core.ConnectedUser;
import fr.geromeavecung.katabankaccount.businessdomain.account.WithdrawMoney;

public class ACustomerWithdrawsMoney {
    private final WithdrawMoney withdrawMoney;
    private final Timestamps timestamps;

    public ACustomerWithdrawsMoney(WithdrawMoney withdrawMoney, Timestamps timestamps) {
        this.withdrawMoney = withdrawMoney;
        this.timestamps = timestamps;
    }

    public void execute(ConnectedUser connectedUser, WithdrawalRequest withdrawalRequest) {
        withdrawMoney.execute(connectedUser, withdrawalRequest.toAmount(), timestamps.now());
    }
}
