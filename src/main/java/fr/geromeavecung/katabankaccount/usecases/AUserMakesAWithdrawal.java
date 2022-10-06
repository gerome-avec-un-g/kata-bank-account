package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.User;
import fr.geromeavecung.katabankaccount.businessdomain.account.WithdrawMoney;

public class AUserMakesAWithdrawal {
    private final WithdrawMoney withdrawMoney;

    public AUserMakesAWithdrawal(WithdrawMoney withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }

    public void execute(User connectedUser, WithdrawalRequest withdrawalRequest) {
        withdrawMoney.execute(connectedUser, withdrawalRequest.toAmount());
    }
}
