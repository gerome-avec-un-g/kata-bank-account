package fr.geromeavecung.katabankaccount.businessdomain.account;

import fr.geromeavecung.katabankaccount.businessdomain.core.Timestamp;

import java.time.YearMonth;

public record Withdrawal(Amount amount, Timestamp timestamp) implements Operation {

    private static final int MINIMUM_AMOUNT = 0;
    private static final int MAXIMUM_AMOUNT = 1000;

    public Withdrawal {
        if (amount.value() <= MINIMUM_AMOUNT) {
            throw new IllegalArgumentException("Withdrawal amount can't be less or equal to 0, was: " + amount.value());
        }
        if (amount.value() > MAXIMUM_AMOUNT) {
            throw new IllegalArgumentException("Withdrawal amount can't be more than 1000, was: " + amount.value());
        }
    }

    public boolean occursInTheSameYearMonth(YearMonth yearMonth) {
        return YearMonth.from(timestamp.value()).equals(yearMonth);
    }

    @Override
    public int signedAmount() {
        return -amount().value();
    }
}
