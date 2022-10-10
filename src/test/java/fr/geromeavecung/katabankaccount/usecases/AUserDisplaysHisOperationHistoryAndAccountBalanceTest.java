package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Account;
import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;
import fr.geromeavecung.katabankaccount.businessdomain.account.Deposit;
import fr.geromeavecung.katabankaccount.businessdomain.account.Operation;
import fr.geromeavecung.katabankaccount.businessdomain.account.OperationsHistory;
import fr.geromeavecung.katabankaccount.businessdomain.account.ReadAccount;
import fr.geromeavecung.katabankaccount.businessdomain.account.Timestamp;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;
import fr.geromeavecung.katabankaccount.businessdomain.account.Withdrawal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AUserDisplaysHisOperationHistoryAndAccountBalanceTest {

    private AccountsInMemory accountsInMemory;

    private AUserDisplaysHisOperationHistoryAndAccountBalance aUserDisplaysHisOperationHistoryAndAccountBalance;

    @BeforeEach
    void setup() {
        accountsInMemory = new AccountsInMemory();
        aUserDisplaysHisOperationHistoryAndAccountBalance = new AUserDisplaysHisOperationHistoryAndAccountBalance(new ReadAccount(accountsInMemory));
    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he displays the account information then the operation history is displayed")
    class operations {
        @Test
        void account_with_1_deposit() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            List<Operation> operations = new ArrayList<>();
            operations.add(new Deposit(new Amount(1), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))));
            Account initialAccount = new Account(user, new OperationsHistory(operations));
            accountsInMemory.save(initialAccount);

            AccountView actual = aUserDisplaysHisOperationHistoryAndAccountBalance.execute(user);

            List<OperationView> expectedOperations = new ArrayList<>();
            expectedOperations.add(new OperationView("TODO operation label!", 1, 1, "2022-09-01T14:07:30"));
            assertThat(actual.operations()).isEqualTo(expectedOperations);
        }

        @Test
        void account_with_1_withdrawal() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            List<Operation> operations = new ArrayList<>();
            operations.add(new Withdrawal(new Amount(1), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))));
            Account initialAccount = new Account(user, new OperationsHistory(operations));
            accountsInMemory.save(initialAccount);

            AccountView actual = aUserDisplaysHisOperationHistoryAndAccountBalance.execute(user);

            List<OperationView> expectedOperations = new ArrayList<>();
            expectedOperations.add(new OperationView("TODO operation label!", -1, -1, "2022-09-01T14:07:30"));
            assertThat(actual.operations()).isEqualTo(expectedOperations);
        }

        @Test
        void account_with_multiple_operations() {
                User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
                List<Operation> operations = new ArrayList<>();
                operations.add(new Deposit(new Amount(10), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))));
                operations.add(new Withdrawal(new Amount(5), new Timestamp(LocalDateTime.parse("2022-09-02T14:07:30"))));
                operations.add(new Withdrawal(new Amount(15), new Timestamp(LocalDateTime.parse("2022-09-03T14:07:30"))));
                operations.add(new Deposit(new Amount(20), new Timestamp(LocalDateTime.parse("2022-09-04T14:07:30"))));
                operations.add(new Deposit(new Amount(25), new Timestamp(LocalDateTime.parse("2022-09-05T14:07:30"))));
                operations.add(new Withdrawal(new Amount(30), new Timestamp(LocalDateTime.parse("2022-09-06T14:07:30"))));
                Account initialAccount = new Account(user, new OperationsHistory(operations));
                accountsInMemory.save(initialAccount);

                AccountView actual = aUserDisplaysHisOperationHistoryAndAccountBalance.execute(user);

                List<OperationView> expectedOperations = new ArrayList<>();
                expectedOperations.add(new OperationView("TODO operation label!", 10, 10, "2022-09-01T14:07:30"));
                expectedOperations.add(new OperationView("TODO operation label!", -5, 5, "2022-09-02T14:07:30"));
                expectedOperations.add(new OperationView("TODO operation label!", -15, -10, "2022-09-03T14:07:30"));
                expectedOperations.add(new OperationView("TODO operation label!", 20, 10, "2022-09-04T14:07:30"));
                expectedOperations.add(new OperationView("TODO operation label!", 25, 35, "2022-09-05T14:07:30"));
                expectedOperations.add(new OperationView("TODO operation label!", -30, 5, "2022-09-06T14:07:30"));
                assertThat(actual.operations()).isEqualTo(expectedOperations);
        }

        @Test
        void account_without_operation() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);

            AccountView actual = aUserDisplaysHisOperationHistoryAndAccountBalance.execute(user);

            assertThat(actual.operations()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he displays the account information then the balance is displayed")
    class balance {
        @Test
        void positive_balance() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            List<Operation> operations = new ArrayList<>();
            operations.add(new Deposit(new Amount(1), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))));
            Account initialAccount = new Account(user, new OperationsHistory(operations));
            accountsInMemory.save(initialAccount);

            AccountView actual = aUserDisplaysHisOperationHistoryAndAccountBalance.execute(user);

            assertThat(actual.balance()).isEqualTo(1);
        }

        @Test
        void negative_balance() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            List<Operation> operations = new ArrayList<>();
            operations.add(new Withdrawal(new Amount(1), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))));
            Account initialAccount = new Account(user, new OperationsHistory(operations));
            accountsInMemory.save(initialAccount);

            AccountView actual = aUserDisplaysHisOperationHistoryAndAccountBalance.execute(user);

            assertThat(actual.balance()).isEqualTo(-1);
        }

        @Test
        void balance_is_0() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);

            AccountView actual = aUserDisplaysHisOperationHistoryAndAccountBalance.execute(user);

            assertThat(actual.balance()).isZero();
        }
    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he displays the account information then the relevant account is displayed")
    class accounts {
        @Test
        void multiple_accounts_for_different_users() {
            User connectedUser = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            User anotherUser = new User(UUID.fromString("edd8d5ee-9af9-491f-bcd9-4fc3a8c4f7d9"));
            List<Operation> operations = new ArrayList<>();
            operations.add(new Deposit(new Amount(1), new Timestamp(LocalDateTime.parse("2022-09-01T14:07:30"))));
            Account initialAccount = new Account(connectedUser, new OperationsHistory(operations));
            accountsInMemory.save(initialAccount);
            Account anotherAccount = new Account(anotherUser);
            accountsInMemory.save(anotherAccount);

            AccountView actual = aUserDisplaysHisOperationHistoryAndAccountBalance.execute(connectedUser);

            assertThat(actual.balance()).isEqualTo(1);
        }

        @Test
        void user_without_accounts() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));

            assertThatThrownBy(() -> aUserDisplaysHisOperationHistoryAndAccountBalance.execute(user))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("user 29516229-e614-4f28-bdfb-ba77cd93e837 has no account");
        }
    }

}
