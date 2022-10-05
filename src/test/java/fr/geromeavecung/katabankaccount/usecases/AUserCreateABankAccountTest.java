package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.Account;
import fr.geromeavecung.katabankaccount.businessdomain.CreateAccount;
import fr.geromeavecung.katabankaccount.businessdomain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class AUserCreateABankAccountTest {

    @Test
    void given_a_connected_user_when_he_create_an_account_without_a_first_deposit_then_the_account_is_created_without_operations() {
        User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        AccountsInMemory accountsInMemory = new AccountsInMemory();
        AUserCreateABankAccount aUserCreateABankAccount = new AUserCreateABankAccount(new CreateAccount(accountsInMemory));
        Account expectedAccount = new Account(user);

        aUserCreateABankAccount.execute(user);

        assertThat(accountsInMemory.getAccountsByUser().values().stream().findFirst().orElseThrow(IllegalStateException::new)).isEqualToComparingFieldByField(expectedAccount);
    }

    @Test
    void given_a_connected_user_when_he_create_an_account_with_a_first_deposit_of_10_euros_then_the_account_is_created_with_a_deposit_operation_of_10_euros() {
        fail();
    }

    @Test
    void given_a_connected_user_when_he_create_an_account_with_a_first_deposit_of_negative_10_euros_then_he_obtains_an_error() {
        fail();
    }

    @Test
    void given_a_connected_user_with_an_account_when_he_create_a_second_account_then_he_obtains_an_error() {
        fail();
    }

}
