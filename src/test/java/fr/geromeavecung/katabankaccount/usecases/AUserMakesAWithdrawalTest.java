package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Account;
import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;
import fr.geromeavecung.katabankaccount.businessdomain.account.Deposit;
import fr.geromeavecung.katabankaccount.businessdomain.account.OperationsHistory;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;
import fr.geromeavecung.katabankaccount.businessdomain.account.WithdrawMoney;
import fr.geromeavecung.katabankaccount.businessdomain.account.Withdrawal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AUserMakesAWithdrawalTest {

    private AccountsInMemory accountsInMemory;

    private AUserMakesAWithdrawal aUserMakesAWithdrawal;

    @BeforeEach
    void setup() {
        accountsInMemory = new AccountsInMemory();
        aUserMakesAWithdrawal = new AUserMakesAWithdrawal(new WithdrawMoney(accountsInMemory));
    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he makes a withdrawal then the new operation is added to the operations history")
    class operations {
        @Test
        void account_with_1_deposit() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory(new Deposit(new Amount(1))));
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Deposit(new Amount(1)), new Withdrawal(new Amount(1))))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1);

            aUserMakesAWithdrawal.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }
    }

}
