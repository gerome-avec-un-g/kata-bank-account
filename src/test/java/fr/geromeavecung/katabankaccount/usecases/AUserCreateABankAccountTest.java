package fr.geromeavecung.katabankaccount.usecases;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class AUserCreateABankAccountTest {

    @Test
    void given_a_connected_user_when_he_create_an_account_without_a_first_deposit_then_the_account_is_created_without_operations() {
        fail();
    }

    @Test
    void given_a_connected_user_when_he_create_an_account_with_a_first_deposit_of_10_euros_then_the_account_is_created_with_a_deposit_operation_of_10_euros() {
        fail();
    }

    @Test
    void given_a_connected_user_when_he_create_an_account_with_a_first_deposit_of_negative_10_euros_then_he_obtains_an_error() {
        fail();
    }

}
