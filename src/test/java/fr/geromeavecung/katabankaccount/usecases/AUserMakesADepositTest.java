package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Account;
import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;
import fr.geromeavecung.katabankaccount.businessdomain.account.Deposit;
import fr.geromeavecung.katabankaccount.businessdomain.account.DepositMoney;
import fr.geromeavecung.katabankaccount.businessdomain.account.OperationsHistory;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class AUserMakesADepositTest {

    private AccountsInMemory accountsInMemory;

    private AUserMakesADeposit aUserMakesADeposit;

    @BeforeEach
    void setup() {
        accountsInMemory = new AccountsInMemory();
        aUserMakesADeposit = new AUserMakesADeposit(new DepositMoney(accountsInMemory));
    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he makes a deposit then the new operation is added to the operations history")
    class operations {
        @Test
        void account_without_operations() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(new Deposit(new Amount(1)))));
            DepositRequest depositRequest = new DepositRequest(1);

            aUserMakesADeposit.execute(user, depositRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void account_with_1_operation() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory(new Deposit(new Amount(2))));
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Deposit(new Amount(2)), new Deposit(new Amount(1))))));
            DepositRequest depositRequest = new DepositRequest(1);

            aUserMakesADeposit.execute(user, depositRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void account_with_multiple_operations() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user, new OperationsHistory(Arrays.asList(new Deposit(new Amount(3)), new Deposit(new Amount(2)))));
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Deposit(new Amount(3)), new Deposit(new Amount(2)), new Deposit(new Amount(1))))));
            DepositRequest depositRequest = new DepositRequest(1);

            aUserMakesADeposit.execute(user, depositRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he make a deposit then the system checks that the deposit amount is greater than 0")
    class depositAmount {
        @Test
        void deposit_amount_is_greater_than_0() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(new Deposit(new Amount(1)))));
            DepositRequest depositRequest = new DepositRequest(1);

            aUserMakesADeposit.execute(user, depositRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void deposit_amount_is_0() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            DepositRequest depositRequest = new DepositRequest(0);

            assertThatThrownBy(() -> aUserMakesADeposit.execute(user, depositRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Deposit amount can't be less or equal to 0, was: 0");
        }

        @Test
        void deposit_amount_is_less_than_0() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            DepositRequest depositRequest = new DepositRequest(-1);

            assertThatThrownBy(() -> aUserMakesADeposit.execute(user, depositRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Deposit amount can't be less or equal to 0, was: -1");
        }
    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he make a deposit then the deposit amount is done on the correct account")
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
            DepositRequest depositRequest = new DepositRequest(1);

            aUserMakesADeposit.execute(user, depositRequest);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
            assertThat(accountsInMemory.forUser(anotherUser)).usingRecursiveComparison()
                    .isEqualTo(anotherExpectedAccount);
        }

        @Test
        void user_without_account() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            DepositRequest depositRequest = new DepositRequest(1);

            assertThatThrownBy(() -> aUserMakesADeposit.execute(user, depositRequest))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("user 29516229-e614-4f28-bdfb-ba77cd93e837 has no account");
        }
    }

}
