package fr.geromeavecung.katabankaccount.usecases.customer;

import fr.geromeavecung.katabankaccount.businessdomain.account.Account;
import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;
import fr.geromeavecung.katabankaccount.businessdomain.account.Deposit;
import fr.geromeavecung.katabankaccount.businessdomain.account.Operation;
import fr.geromeavecung.katabankaccount.businessdomain.account.OperationsHistory;
import fr.geromeavecung.katabankaccount.businessdomain.core.Timestamp;
import fr.geromeavecung.katabankaccount.businessdomain.core.ConnectedUser;
import fr.geromeavecung.katabankaccount.businessdomain.account.WithdrawMoney;
import fr.geromeavecung.katabankaccount.businessdomain.account.Withdrawal;
import fr.geromeavecung.katabankaccount.usecases.AccountsInMemory;
import fr.geromeavecung.katabankaccount.usecases.FixedTimestamps;
import fr.geromeavecung.katabankaccount.usecases.customer.withdrawsmoney.ACustomerWithdrawsMoney;
import fr.geromeavecung.katabankaccount.usecases.customer.withdrawsmoney.WithdrawalRequest;
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

public class ACustomerWithdrawsMoneyTest {

    private AccountsInMemory accountsInMemory;

    private FixedTimestamps fixedTimestamps;

    private ACustomerWithdrawsMoney ACustomerWithdrawsMoney;

    @BeforeEach
    void setup() {
        accountsInMemory = new AccountsInMemory();
        fixedTimestamps = new FixedTimestamps();
        fixedTimestamps.setTimestamp("2022-10-06T14:07:30");
        ACustomerWithdrawsMoney = new ACustomerWithdrawsMoney(new WithdrawMoney(accountsInMemory), fixedTimestamps);
    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he makes a withdrawal then the new operation is added to the operations history")
    class operations {
        @Test
        void account_with_1_deposit() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory(new Deposit(new Amount(1), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30")))));
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Deposit(new Amount(1), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))), new Withdrawal(new Amount(2), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30")))))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(2);

            ACustomerWithdrawsMoney.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void account_with_1_withdrawal() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory(new Withdrawal(new Amount(1), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30")))));
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Withdrawal(new Amount(1), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30"))), new Withdrawal(new Amount(2), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30")))))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(2);

            ACustomerWithdrawsMoney.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        // TODO tests on timestamps

        @Test
        void account_without_operations() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory());
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(new Withdrawal(new Amount(2), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30"))))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(2);

            ACustomerWithdrawsMoney.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }
    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he make a withdrawal then the system checks if the withdrawal is authorized")
    class withdrawalAmount {
        @Test
        void withdrawal_amount_is_greater_than_0() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(new Withdrawal(new Amount(1), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30"))))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1);

            ACustomerWithdrawsMoney.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void withdrawal_amount_is_0() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(0);

            assertThatThrownBy(() -> ACustomerWithdrawsMoney.execute(user, withdrawalRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Withdrawal amount can't be less or equal to 0, was: 0");
        }

        @Test
        void withdrawal_amount_is_less_than_0() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(-1);

            assertThatThrownBy(() -> ACustomerWithdrawsMoney.execute(user, withdrawalRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Withdrawal amount can't be less or equal to 0, was: -1");
        }

        @Test
        void withdrawal_maximum_amount() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory(new Deposit(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30")))));
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Deposit(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))), new Withdrawal(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30")))))));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1000);

            ACustomerWithdrawsMoney.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void withdrawal_more_than_maximum() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory(new Deposit(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30")))));
            accountsInMemory.save(initialAccount);
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1001);

            assertThatThrownBy(() -> ACustomerWithdrawsMoney.execute(user, withdrawalRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Withdrawal amount can't be more than 1000, was: 1001");
        }

        @Test
        void withdrawal_maximum_amount_per_month() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
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

            ACustomerWithdrawsMoney.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        // TODO take into account year month not just year

        @Test
        void withdrawal_more_than_maximum_per_month() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            List<Operation> operations = new ArrayList<>();
            operations.add(new Deposit(new Amount(10000), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))));
            operations.add(new Withdrawal(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-06T14:07:30"))));
            operations.add(new Withdrawal(new Amount(1000), new Timestamp(LocalDateTime.parse("2022-09-30T23:59:59"))));
            Account initialAccount = new Account(user, new OperationsHistory(operations));
            accountsInMemory.save(initialAccount);
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1);
            fixedTimestamps.setTimestamp("2022-09-30T23:59:59");

            assertThatThrownBy(() -> ACustomerWithdrawsMoney.execute(user, withdrawalRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Sum of withdrawal amount can't be more than 2000 per month, was: 2001");
        }

        @Test
        void withdrawal_more_than_maximum_per_month_on_two_months() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
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


            ACustomerWithdrawsMoney.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void withdrawal_maximum_amount_per_account() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            List<Operation> expectedOperations = new ArrayList<>();
            expectedOperations.add(new Withdrawal(new Amount(100), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30"))));
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(expectedOperations)));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(100);

            ACustomerWithdrawsMoney.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        // TODO more tests on balance computing : multiple deposits and withdrawal

        @Test
        void withdrawal_more_than_maximum_amount_per_account() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(101);

            assertThatThrownBy(() -> ACustomerWithdrawsMoney.execute(user, withdrawalRequest))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("balance of account can't be below -100, was: -101");
        }

    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he make a withdrawal then the withdrawal amount is done on the correct account")
    class account {
        @Test
        void accounts_for_different_users() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            ConnectedUser anotherUser = new ConnectedUser(UUID.fromString("edd8d5ee-9af9-491f-bcd9-4fc3a8c4f7d9"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            Account anotherInitialAccount = new Account(anotherUser);
            accountsInMemory.save(anotherInitialAccount);
            List<Operation> expectedOperations = new ArrayList<>();
            expectedOperations.add(new Withdrawal(new Amount(1), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30"))));
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(expectedOperations)));
            Optional<Account> anotherExpectedAccount = Optional.of(new Account(anotherUser, new OperationsHistory()));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1);

            ACustomerWithdrawsMoney.execute(user, withdrawalRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
            assertThat(accountsInMemory.forUser(anotherUser)).usingRecursiveComparison()
                    .isEqualTo(anotherExpectedAccount);
        }

        @Test
        void user_without_account() {
            ConnectedUser user = new ConnectedUser(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest(1);

            assertThatThrownBy(() -> ACustomerWithdrawsMoney.execute(user, withdrawalRequest))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("user 29516229-e614-4f28-bdfb-ba77cd93e837 has no account");
        }
    }

}
