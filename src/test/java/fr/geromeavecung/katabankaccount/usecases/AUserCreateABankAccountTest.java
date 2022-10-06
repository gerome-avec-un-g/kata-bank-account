package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Account;
import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;
import fr.geromeavecung.katabankaccount.businessdomain.account.CreateAccount;
import fr.geromeavecung.katabankaccount.businessdomain.account.Deposit;
import fr.geromeavecung.katabankaccount.businessdomain.account.OperationsHistory;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class AUserCreateABankAccountTest {

    @Test
    void given_a_connected_user_when_he_create_an_account_without_a_first_deposit_then_the_account_is_created_without_operations() {
        User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        AccountsInMemory accountsInMemory = new AccountsInMemory();
        AUserCreateABankAccount aUserCreateABankAccount = new AUserCreateABankAccount(new CreateAccount(accountsInMemory));
        Account expectedAccount = new Account(user);
        AccountCreationForm accountCreationForm = new AccountCreationForm(0);

        aUserCreateABankAccount.execute(user, accountCreationForm);

        assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                .isEqualTo(expectedAccount);
    }

    @Test
    void given_a_connected_user_when_he_create_an_account_with_a_first_deposit_of_1_euro_then_the_account_is_created_with_a_deposit_operation_of_1_euro() {
        User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        AccountsInMemory accountsInMemory = new AccountsInMemory();
        AUserCreateABankAccount aUserCreateABankAccount = new AUserCreateABankAccount(new CreateAccount(accountsInMemory));
        Deposit firstDeposit = new Deposit(new Amount(1));
        OperationsHistory operationsHistory = new OperationsHistory(firstDeposit);
        Optional<Account> expectedAccount = Optional.of(new Account(user, operationsHistory));
        AccountCreationForm accountCreationForm = new AccountCreationForm(1);

        aUserCreateABankAccount.execute(user, accountCreationForm);

        assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                .isEqualTo(expectedAccount);
    }

    @Test
    void given_a_connected_user_when_he_create_an_account_with_a_first_deposit_of_negative_1_euros_then_he_obtains_an_error() {
        User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        AccountsInMemory accountsInMemory = new AccountsInMemory();
        AUserCreateABankAccount aUserCreateABankAccount = new AUserCreateABankAccount(new CreateAccount(accountsInMemory));
        AccountCreationForm accountCreationForm = new AccountCreationForm(-1);

        assertThatThrownBy(() -> aUserCreateABankAccount.execute(user, accountCreationForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Deposit amount can't be less or equal to 0");
        }

    @Test
    void given_a_connected_user_with_an_account_when_he_create_a_second_account_then_he_obtains_an_error() {
        User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        AccountsInMemory accountsInMemory = new AccountsInMemory();
        Account connectedUserAccount = new Account(user);
        accountsInMemory.save(connectedUserAccount);
        AUserCreateABankAccount aUserCreateABankAccount = new AUserCreateABankAccount(new CreateAccount(accountsInMemory));
        AccountCreationForm accountCreationForm = new AccountCreationForm(-1);

        assertThatThrownBy(() -> aUserCreateABankAccount.execute(user, accountCreationForm))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("a user can't have two accounts");
    }

    @Test
    void given_a_connected_user_with_no_account_and_an_account_for_another_user_when_he_create_an_account_without_a_first_deposit_then_the_account_is_created_without_operations() {
        User connectedUser = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        User anotherUser = new User(UUID.fromString("edd8d5ee-9af9-491f-bcd9-4fc3a8c4f7d9"));
        AccountsInMemory accountsInMemory = new AccountsInMemory();
        Account connectedUserAccount = new Account(anotherUser);
        accountsInMemory.save(connectedUserAccount);
        AUserCreateABankAccount aUserCreateABankAccount = new AUserCreateABankAccount(new CreateAccount(accountsInMemory));
        Optional<Account> expectedAccount = Optional.of(new Account(connectedUser));
        AccountCreationForm accountCreationForm = new AccountCreationForm(0);

        aUserCreateABankAccount.execute(connectedUser, accountCreationForm);

        assertThat(accountsInMemory.forUser(connectedUser)).usingRecursiveComparison()
                .isEqualTo(expectedAccount);
    }

    // TODO tests about user : no user/empty user/bad uuid/unknown uuid...
    // TODO tests about amount : maximum number ? is there a minimum if not 0 (either 0 or 100+ for exemple)
    // TODO other feature ? : a bank admin creates an account for a user
}
