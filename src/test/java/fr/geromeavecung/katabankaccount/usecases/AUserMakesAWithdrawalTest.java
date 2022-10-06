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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Deposit(new Amount(1)), new Withdrawal(new Amount(2))))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(2);

            aUserMakesAWithdrawal.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void account_with_1_withdrawal() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory(new Withdrawal(new Amount(1))));
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Withdrawal(new Amount(1)), new Withdrawal(new Amount(2))))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(2);

            aUserMakesAWithdrawal.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void account_without_operations() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory());
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(new Withdrawal(new Amount(2)))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(2);

            aUserMakesAWithdrawal.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }
    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he make a withdrawal then the system checks that the withdrawal amount is greater than 0")
    class withdrawalAmount {
        @Test
        void withdrawal_amount_is_greater_than_0() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(new Withdrawal(new Amount(1)))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1);

            aUserMakesAWithdrawal.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void withdrawal_amount_is_0() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(0);

            assertThatThrownBy(() -> aUserMakesAWithdrawal.execute(user, withdrawalRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Withdrawal amount can't be less or equal to 0, was: 0");
        }

        @Test
        void withdrawal_amount_is_less_than_0() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(-1);

            assertThatThrownBy(() -> aUserMakesAWithdrawal.execute(user, withdrawalRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Withdrawal amount can't be less or equal to 0, was: -1");
        }
    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he make a withdrawal then the withdrawal amount is done on the correct account")
    class account {
        @Test
        void accounts_for_different_users() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            User anotherUser = new User(UUID.fromString("edd8d5ee-9af9-491f-bcd9-4fc3a8c4f7d9"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            Account anotherInitialAccount = new Account(anotherUser);
            accountsInMemory.save(anotherInitialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(new Deposit(new Amount(1)))));
            Optional<Account> anotherExpectedAccount = Optional.of(new Account(anotherUser, new OperationsHistory()));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1);

            aUserMakesAWithdrawal.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
            assertThat(accountsInMemory.forUser(anotherUser)).usingRecursiveComparison()
                    .isEqualTo(anotherExpectedAccount);
        }

        @Test
        void user_without_account() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1);

            assertThatThrownBy(() -> aUserMakesAWithdrawal.execute(user, withdrawalRequest))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("user 29516229-e614-4f28-bdfb-ba77cd93e837 has no account");
        }
    }

}
