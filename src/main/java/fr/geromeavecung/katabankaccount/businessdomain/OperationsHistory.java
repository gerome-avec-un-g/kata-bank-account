package fr.geromeavecung.katabankaccount.businessdomain;

public class OperationsHistory {
    private final Deposit deposit;

    public OperationsHistory() {
        this.deposit = null; //FIXME
    }

    public OperationsHistory(Deposit deposit) {
        this.deposit = deposit;
    }
}
