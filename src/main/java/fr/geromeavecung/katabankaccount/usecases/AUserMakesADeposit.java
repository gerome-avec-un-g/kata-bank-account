package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.DepositMoney;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;

public class AUserMakesADeposit {

    private final DepositMoney depositMoney;

    public AUserMakesADeposit(DepositMoney depositMoney) {
        this.depositMoney = depositMoney;
    }

    public void execute(User user, DepositForm depositForm) {
        depositMoney.execute(user, depositForm.toAmount());
    }
}
