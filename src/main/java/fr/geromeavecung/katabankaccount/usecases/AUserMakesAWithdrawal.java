package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Timestamps;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;
import fr.geromeavecung.katabankaccount.businessdomain.account.WithdrawMoney;

public class AUserMakesAWithdrawal {
    private final WithdrawMoney withdrawMoney;
    private final Timestamps timestamps;

    public AUserMakesAWithdrawal(WithdrawMoney withdrawMoney, Timestamps timestamps) {
        this.withdrawMoney = withdrawMoney;
        this.timestamps = timestamps;
    }

    public void execute(User connectedUser, WithdrawalRequest withdrawalRequest) {
        withdrawMoney.execute(connectedUser, withdrawalRequest.toAmount(), timestamps.now());
    }
}
