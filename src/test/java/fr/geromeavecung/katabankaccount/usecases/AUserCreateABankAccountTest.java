package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.Account;
import fr.geromeavecung.katabankaccount.businessdomain.Amount;
import fr.geromeavecung.katabankaccount.businessdomain.CreateAccount;
import fr.geromeavecung.katabankaccount.businessdomain.Deposit;
import fr.geromeavecung.katabankaccount.businessdomain.OperationsHistory;
import fr.geromeavecung.katabankaccount.businessdomain.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

public class AUserCreateABankAccountTest {

    @Test
    void given_a_connected_user_when_he_create_an_account_without_a_first_deposit_then_the_account_is_created_without_operations() {
        User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        AccountsInMemory accountsInMemory = new AccountsInMemory();
        AUserCreateABankAccount aUserCreateABankAccount = new AUserCreateABankAccount(new CreateAccount(accountsInMemory));
        Account expectedAccount = new Account(user);
        AccountCreationForm accountCreationForm = new AccountCreationForm(0);

        aUserCreateABankAccount.execute(user, accountCreationForm);

        assertThat(accountsInMemory.getAccountsByUser().values().stream().findFirst().orElseThrow(IllegalStateException::new)).isEqualToComparingFieldByFieldRecursively(expectedAccount);
    }

    @Test
    void given_a_connected_user_when_he_create_an_account_with_a_first_deposit_of_10_euros_then_the_account_is_created_with_a_deposit_operation_of_10_euros() {
        User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        AccountsInMemory accountsInMemory = new AccountsInMemory();
        AUserCreateABankAccount aUserCreateABankAccount = new AUserCreateABankAccount(new CreateAccount(accountsInMemory));
        Deposit firstDeposit = new Deposit(new Amount(10));
        OperationsHistory operationsHistory = new OperationsHistory(firstDeposit);
        Account expectedAccount = new Account(user, operationsHistory);
        AccountCreationForm accountCreationForm = new AccountCreationForm(10);

        aUserCreateABankAccount.execute(user, accountCreationForm);

        assertThat(accountsInMemory.getAccountsByUser().values().stream().findFirst().orElseThrow(IllegalStateException::new)).isEqualToComparingFieldByFieldRecursively(expectedAccount);
    }

    @Test
    void given_a_connected_user_when_he_create_an_account_with_a_first_deposit_of_negative_10_euros_then_he_obtains_an_error() {
        User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        AccountsInMemory accountsInMemory = new AccountsInMemory();
        AUserCreateABankAccount aUserCreateABankAccount = new AUserCreateABankAccount(new CreateAccount(accountsInMemory));
        Deposit firstDeposit = new Deposit(new Amount(10));
        OperationsHistory operationsHistory = new OperationsHistory(firstDeposit);
        AccountCreationForm accountCreationForm = new AccountCreationForm(-10);

        assertThatThrownBy(() -> aUserCreateABankAccount.execute(user, accountCreationForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Deposit amount can't be less or equal to 0");
        }

    @Test
    void given_a_connected_user_with_an_account_when_he_create_a_second_account_then_he_obtains_an_error() {
        fail();
    }

    // TODO : no user/empty user/bad uuid/unknown uuid...

}
