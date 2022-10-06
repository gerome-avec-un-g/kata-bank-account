package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Account;
import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;
import fr.geromeavecung.katabankaccount.businessdomain.account.Deposit;
import fr.geromeavecung.katabankaccount.businessdomain.account.DepositMoney;
import fr.geromeavecung.katabankaccount.businessdomain.account.OperationsHistory;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class AUserMakesADepositTest {

    @Nested
    @DisplayName("Given a connected user with a bank account when he makes a deposit then the new operation is added to the operations history")
    class operations {
        @Test
        void account_without_operations() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            AccountsInMemory accountsInMemory = new AccountsInMemory();
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(new Deposit(new Amount(1)))));
            DepositForm depositForm = new DepositForm(1);
            AUserMakesADeposit aUserMakesADeposit = new AUserMakesADeposit(new DepositMoney(accountsInMemory));

            aUserMakesADeposit.execute(user, depositForm);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void account_with_1_operation() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            AccountsInMemory accountsInMemory = new AccountsInMemory();
            Account initialAccount = new Account(user, new OperationsHistory(new Deposit(new Amount(2))));
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Deposit(new Amount(2)), new Deposit(new Amount(1))))));
            DepositForm depositForm = new DepositForm(1);
            AUserMakesADeposit aUserMakesADeposit = new AUserMakesADeposit(new DepositMoney(accountsInMemory));

            aUserMakesADeposit.execute(user, depositForm);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void account_with_multiple_operations() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            AccountsInMemory accountsInMemory = new AccountsInMemory();
            Account initialAccount = new Account(user, new OperationsHistory(Arrays.asList(new Deposit(new Amount(3)), new Deposit(new Amount(2)))));
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Deposit(new Amount(3)), new Deposit(new Amount(2)), new Deposit(new Amount(1))))));
            DepositForm depositForm = new DepositForm(1);
            AUserMakesADeposit aUserMakesADeposit = new AUserMakesADeposit(new DepositMoney(accountsInMemory));

            aUserMakesADeposit.execute(user, depositForm);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

    }

    @Nested
    @DisplayName("Given a connected user with a bank account when he make a deposit then the system checks that the deposit amount is greater than 0")
    class depositAmount {
        @Test
        void deposit_amout_is_greater_than_0() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            AccountsInMemory accountsInMemory = new AccountsInMemory();
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(new Deposit(new Amount(1)))));
            DepositForm depositForm = new DepositForm(1);
            AUserMakesADeposit aUserMakesADeposit = new AUserMakesADeposit(new DepositMoney(accountsInMemory));

            aUserMakesADeposit.execute(user, depositForm);

            assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                    .isEqualTo(expectedAccount);
        }

        @Test
        void deposit_amout_is_0() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            AccountsInMemory accountsInMemory = new AccountsInMemory();
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            DepositForm depositForm = new DepositForm(0);
            AUserMakesADeposit aUserMakesADeposit = new AUserMakesADeposit(new DepositMoney(accountsInMemory));

            assertThatThrownBy(() -> aUserMakesADeposit.execute(user, depositForm))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Deposit amount can't be less or equal to 0, was: 0");
        }

        @Test
        void deposit_amount_is_less_than_0() {
            User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
            AccountsInMemory accountsInMemory = new AccountsInMemory();
            Account initialAccount = new Account(user);
            accountsInMemory.save(initialAccount);
            DepositForm depositForm = new DepositForm(-1);
            AUserMakesADeposit aUserMakesADeposit = new AUserMakesADeposit(new DepositMoney(accountsInMemory));

            assertThatThrownBy(() -> aUserMakesADeposit.execute(user, depositForm))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Deposit amount can't be less or equal to 0, was: -1");
        }
    }
    // TODO 2 account, deposit on 1 only
    // TODO user without account

}
