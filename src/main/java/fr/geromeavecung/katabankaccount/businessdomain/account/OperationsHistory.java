package fr.geromeavecung.katabankaccount.businessdomain.account;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OperationsHistory {
    private static final int MAXIMUM_PER_MONTH = 2000;
    private final List<Operation> operations;

    public OperationsHistory() {
        this.operations = new ArrayList<>();
    }

    public OperationsHistory(Operation operation) {
        this.operations = new ArrayList<>();
        add(operation);
    }

    public OperationsHistory(List<Operation> operations) {
        // new ArrayList because parameter can be an immutable list
        this.operations = new ArrayList<>(operations);
    }

    public void add(Operation operation) {
        int totalMonthAmount = currentMonthWithdrawalAmountsSum(operation) + operation.amount().value();
        if (totalMonthAmount > MAXIMUM_PER_MONTH) {
            throw new IllegalArgumentException("Sum of withdrawal amount can't be more than 2000 per month, was: " + totalMonthAmount);
        }
        operations.add(operation);
    }

    private int currentMonthWithdrawalAmountsSum(Operation operation) {
        YearMonth yearMonthOfOperation = YearMonth.from(operation.timestamp().value());
        return operations.stream()
                .filter(Withdrawal.class::isInstance)
                .map(Withdrawal.class::cast)
                .filter(withdrawal -> withdrawal.occursInTheSameYearMonth(yearMonthOfOperation))
                .mapToInt(withdrawal -> withdrawal.amount().value())
                .sum();
    }

    public int expectedBalance(Withdrawal expectedWithdrawal) {
        return balance() - expectedWithdrawal.amount().value();
    }

    private int balance() {
        return operations.stream().mapToInt(Operation::signedAmount).sum();
    }

    public List<Operation> getOperations() {
        // return a new instance to prevent data manipulation
        return Collections.unmodifiableList(operations);
    }
}
