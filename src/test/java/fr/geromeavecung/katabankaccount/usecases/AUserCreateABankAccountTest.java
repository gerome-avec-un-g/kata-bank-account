package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Account;
import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;
import fr.geromeavecung.katabankaccount.businessdomain.account.CreateAccount;
import fr.geromeavecung.katabankaccount.businessdomain.account.Deposit;
import fr.geromeavecung.katabankaccount.businessdomain.account.OperationsHistory;
import fr.geromeavecung.katabankaccount.businessdomain.account.Timestamp;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AUserCreateABankAccountTest {

    private AccountsInMemory accountsInMemory;

    private AUserCreateABankAccount aUserCreateABankAccount;

    private FixedTimestamps fixedTimestamps;

    @BeforeEach
    void setup() {
        accountsInMemory = new AccountsInMemory();
        fixedTimestamps = new FixedTimestamps();
        fixedTimestamps.setTimestamp("2022-10-06T14:07:30");
        aUserCreateABankAccount = new AUserCreateABankAccount(new CreateAccount(accountsInMemory, fixedTimestamps));

    }
    @Test
    void given_a_connected_user_when_he_create_an_account_without_a_first_deposit_then_the_account_is_created_without_operations() {
        User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        Optional<Account> expectedAccount = Optional.of(new Account(user));
        AccountCreationRequest accountCreationRequest = new AccountCreationRequest(0);

        aUserCreateABankAccount.execute(user, accountCreationRequest);

        assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                .isEqualTo(expectedAccount);
    }

    @Test
    void given_a_connected_user_when_he_create_an_account_with_a_first_deposit_of_1_euro_then_the_account_is_created_with_a_deposit_operation_of_1_euro() {
        User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        Deposit firstDeposit = new Deposit(new Amount(1), new Timestamp(LocalDateTime.parse("2022-10-06T14:07:30")));
        OperationsHistory operationsHistory = new OperationsHistory(firstDeposit);
        Optional<Account> expectedAccount = Optional.of(new Account(user, operationsHistory));
        AccountCreationRequest accountCreationRequest = new AccountCreationRequest(1);

        aUserCreateABankAccount.execute(user, accountCreationRequest);

        assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                .isEqualTo(expectedAccount);
    }

    // TODO tests on timestamps

    @Test
    void given_a_connected_user_when_he_create_an_account_with_a_first_deposit_of_negative_1_euros_then_he_obtains_an_error() {
        User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        AccountCreationRequest accountCreationRequest = new AccountCreationRequest(-1);

        assertThatThrownBy(() -> aUserCreateABankAccount.execute(user, accountCreationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Deposit amount can't be less or equal to 0, was: -1");
    }

    @Test
    void given_a_connected_user_with_an_account_when_he_create_a_second_account_then_he_obtains_an_error() {
        User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        Account connectedUserAccount = new Account(user);
        accountsInMemory.save(connectedUserAccount);
        AccountCreationRequest accountCreationRequest = new AccountCreationRequest(-1);

        assertThatThrownBy(() -> aUserCreateABankAccount.execute(user, accountCreationRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("a user can't have two accounts");
    }

    @Test
    void given_a_connected_user_with_no_account_and_an_account_for_another_user_when_he_create_an_account_without_a_first_deposit_then_the_account_is_created_without_operations() {
        User connectedUser = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        User anotherUser = new User(UUID.fromString("edd8d5ee-9af9-491f-bcd9-4fc3a8c4f7d9"));
        Account connectedUserAccount = new Account(anotherUser);
        accountsInMemory.save(connectedUserAccount);
        Optional<Account> expectedAccount = Optional.of(new Account(connectedUser));
        AccountCreationRequest accountCreationRequest = new AccountCreationRequest(0);

        aUserCreateABankAccount.execute(connectedUser, accountCreationRequest);

        assertThat(accountsInMemory.forUser(connectedUser)).usingRecursiveComparison()
                .isEqualTo(expectedAccount);
    }

    // TODO tests about user : no user/empty user/bad uuid/unknown uuid...
    // TODO tests about amount : maximum number ? is there a minimum if not 0 (either 0 or 100+ for example)
    // TODO other feature ? : a bank admin creates an account for a user
}
