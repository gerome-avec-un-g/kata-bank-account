package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Account;
import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;
import fr.geromeavecung.katabankaccount.businessdomain.account.Deposit;
import fr.geromeavecung.katabankaccount.businessdomain.account.Operation;
import fr.geromeavecung.katabankaccount.businessdomain.account.OperationsHistory;
import fr.geromeavecung.katabankaccount.businessdomain.account.Timestamp;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;
import fr.geromeavecung.katabankaccount.businessdomain.account.WithdrawMoney;
import fr.geromeavecung.katabankaccount.businessdomain.account.Withdrawal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class AUserMakesAWithdrawalTest {

    private AccountsInMemory accountsInMemory;

    private FixedTimestamps fixedTimestamps;

    private AUserMakesAWithdrawal aUserMakesAWithdrawal;

    @BeforeEach
    void setup() {
        accountsInMemory = new AccountsInMemory();
        fixedTimestamps = new FixedTimestamps();
        fixedTimestamps.setTimestamp("2022-10-06T14:07:30");
        aUserMakesAWithdrawal = new AUserMakesAWithdrawal(new WithdrawMoney(accountsInMemory), fixedTimestamps);
    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he makes a withdrawal then the new operation is added to the operations history")
    class operations {
        @Test
        void account_with_1_deposit() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory(new Deposit(new Amount(1), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30")))));
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Deposit(new Amount(1), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))), new Withdrawal(new Amount(2), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30")))))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(2);

            aUserMakesAWithdrawal.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void account_with_1_withdrawal() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory(new Withdrawal(new Amount(1), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30")))));
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Withdrawal(new Amount(1), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30"))), new Withdrawal(new Amount(2), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30")))))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(2);

            aUserMakesAWithdrawal.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        // TODO tests on timestamps

        @Test
        void account_without_operations() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory());
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(new Withdrawal(new Amount(2), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30"))))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(2);

            aUserMakesAWithdrawal.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }
    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he make a withdrawal then the system checks if the withdrawal is authorized")
    class withdrawalAmount {
        @Test
        void withdrawal_amount_is_greater_than_0() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(new Withdrawal(new Amount(1), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30"))))));
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

        @Test
        void withdrawal_maximum_amount() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory(new Deposit(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30")))));
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Deposit(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))), new Withdrawal(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30")))))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1000);

            aUserMakesAWithdrawal.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void withdrawal_more_than_maximum() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory(new Deposit(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30")))));
            accountsInMemory.save(initialAccount);
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1001);

            assertThatThrownBy(() -> aUserMakesAWithdrawal.execute(user, withdrawalRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Withdrawal amount can't be more than 1000, was: 1001");
        }

        @Test
        void withdrawal_maximum_amount_per_month() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            List<Operation> operations = new ArrayList<>();
            operations.add(new Deposit(new Amount(10000), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))));
            operations.add(new Withdrawal(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-06T14:07:30"))));
            operations.add(new Withdrawal(new Amount(999), new Timestamp(LocalDateTime.parse("2022-09-30T23:59:58"))));
            Account initialAccount = new Account(user, new OperationsHistory(operations));
            accountsInMemory.save(initialAccount);
            List<Operation> expectedOperations = new ArrayList<>();
            expectedOperations.add(new Deposit(new Amount(10000), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))));
            expectedOperations.add(new Withdrawal(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-06T14:07:30"))));
            expectedOperations.add(new Withdrawal(new Amount(999), new Timestamp(LocalDateTime.parse("2022-09-30T23:59:58"))));
            expectedOperations.add(new Withdrawal(new Amount(1), new Timestamp(LocalDateTime.parse("2022-09-30T23:59:59"))));
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(expectedOperations)));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1);
            fixedTimestamps.setTimestamp("2022-09-30T23:59:59");

            aUserMakesAWithdrawal.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        // TODO take into account yearmonth not just year

        @Test
        void withdrawal_more_than_maximum_per_month() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            List<Operation> operations = new ArrayList<>();
            operations.add(new Deposit(new Amount(10000), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))));
            operations.add(new Withdrawal(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-06T14:07:30"))));
            operations.add(new Withdrawal(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-30T23:59:59"))));
            Account initialAccount = new Account(user, new OperationsHistory(operations));
            accountsInMemory.save(initialAccount);
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1);
            fixedTimestamps.setTimestamp("2022-09-30T23:59:59");

            assertThatThrownBy(() -> aUserMakesAWithdrawal.execute(user, withdrawalRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Sum of withdrawal amount can't be more than 2000 per month, was: 2001");
        }

        @Test
        void withdrawal_more_than_maximum_per_month_on_two_months() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            List<Operation> operations = new ArrayList<>();
            operations.add(new Deposit(new Amount(10000), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))));
            operations.add(new Withdrawal(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-06T14:07:30"))));
            operations.add(new Withdrawal(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-30T23:59:58"))));
            Account initialAccount = new Account(user, new OperationsHistory(operations));
            accountsInMemory.save(initialAccount);
            List<Operation> expectedOperations = new ArrayList<>();
            expectedOperations.add(new Deposit(new Amount(10000), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))));
            expectedOperations.add(new Withdrawal(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-06T14:07:30"))));
            expectedOperations.add(new Withdrawal(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-30T23:59:58"))));
            expectedOperations.add(new Withdrawal(new Amount(1), new Timestamp(LocalDateTime.parse("2022-10-01T00:00:00"))));
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(expectedOperations)));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1);
            fixedTimestamps.setTimestamp("2022-10-01T00:00:00");


            aUserMakesAWithdrawal.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }
        // TODO max withdrawal on account
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
            List<Operation> expectedOperations = new ArrayList<>();
            expectedOperations.add(new Withdrawal(new Amount(1), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30"))));
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(expectedOperations)));
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
