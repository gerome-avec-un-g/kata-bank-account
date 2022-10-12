package fr.geromeavecung.katabankaccount.businessdomain.account;

import fr.geromeavecung.katabankaccount.businessdomain.core.Timestamp;

public interface Operation {
    Amount amount();

    Timestamp timestamp();

    int signedAmount();
}
