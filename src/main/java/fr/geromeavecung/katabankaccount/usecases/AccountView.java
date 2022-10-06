package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Account;

import java.util.List;

public record AccountView(List<OperationView> operations) {

    public AccountView(Account account) {
        this(account.getOperations().stream()
                .map(OperationView::new)
                .toList());
    }

}
