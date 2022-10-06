package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Account;
import fr.geromeavecung.katabankaccount.businessdomain.account.Amount;
import fr.geromeavecung.katabankaccount.businessdomain.account.Deposit;
import fr.geromeavecung.katabankaccount.businessdomain.account.DepositMoney;
import fr.geromeavecung.katabankaccount.businessdomain.account.OperationsHistory;
import fr.geromeavecung.katabankaccount.businessdomain.account.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AUserMakesADepositTest {

    @Test
    void given_a_connected_user_with_an_account_without_operations_when_he_deposit_1_euro_then_the_account_is_credited_with_1_euro() {
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
    void given_a_connected_user_with_an_account_with_1_operation_when_he_deposit_2_euros_then_the_account_is_credited_with_2_euros() {
        User user = new User(UUID.fromString("29516229-e614-4f28-bdfb-ba77cd93e837"));
        AccountsInMemory accountsInMemory = new AccountsInMemory();
        Account initialAccount = new Account(user, new OperationsHistory(new Deposit(new Amount(1))));
        accountsInMemory.save(initialAccount);
        Optional<Account> expectedAccount = Optional.of(new Account(user, new OperationsHistory(Arrays.asList(new Deposit(new Amount(1)), new Deposit(new Amount(2))))));
        DepositForm depositForm = new DepositForm(2);
        AUserMakesADeposit aUserMakesADeposit = new AUserMakesADeposit(new DepositMoney(accountsInMemory));

        aUserMakesADeposit.execute(user, depositForm);

        assertThat(accountsInMemory.forUser(user)).usingRecursiveComparison()
                .isEqualTo(expectedAccount);
    }
// TODO  2 existing operations ? does it add something to the behavior ?
    // TODO deposit limits -1 0 1
    // TODO 2 account, deposit on 1 only
    // TODO user without account

}
