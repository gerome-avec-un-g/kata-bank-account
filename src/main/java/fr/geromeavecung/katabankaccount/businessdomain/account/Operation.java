package fr.geromeavecung.katabankaccount.businessdomain.account;

public interface Operation {
    Amount amount();

    Timestamp timestamp();

    int signedAmount();
}
