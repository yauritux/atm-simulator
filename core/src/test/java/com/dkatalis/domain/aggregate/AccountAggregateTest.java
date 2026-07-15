package com.dkatalis.domain.aggregate;

import com.dkatalis.domain.entity.CustomerAccount;
import com.dkatalis.domain.port.out.FakeCustomerAccountRepository;
import com.dkatalis.domain.port.out.FakeDebtAccountRepository;
import com.dkatalis.domain.valueobject.TransactionResponse;
import com.dkatalis.sharedkernel.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountAggregateTest {

    private AccountAggregate accountService;
    private FakeCustomerAccountRepository accountRepository;
    private FakeDebtAccountRepository debtAccountRepository;


    @BeforeEach
    void setUp() {
        accountRepository = new FakeCustomerAccountRepository();
        debtAccountRepository = new FakeDebtAccountRepository();
        accountService = new AccountAggregate(accountRepository, debtAccountRepository);
    }

    @Test
    void loginWithNewAccountCreatesNewCustomer() {
        BigDecimal balance = accountService.login("yauritux");
        assertEquals(BigDecimal.ZERO, balance);
        assertEquals("yauritux", accountService.getCurrentAccount().getName());
    }

    @Test
    void loginWithExistingAccountReturnsExistingBalance() {
        var account = new CustomerAccount("yauritux", BigDecimal.valueOf(100));
        accountRepository.save(account);

        BigDecimal balance = accountService.login("yauritux");
        assertEquals(BigDecimal.valueOf(100), balance);
    }

    @Test
    void logoutShouldClearCurrentAccount() {
        accountService.login("yauritux");
        assertNotNull(accountService.getCurrentAccount());
        accountService.logout();
        assertNull(accountService.getCurrentAccount());
    }

    @Test
    void depositWithoutLoginShouldFail() {
        assertThrows(DomainException.class, () -> accountService.deposit(BigDecimal.valueOf(200)));
    }

    @Test
    void depositWithNegativeAmountShouldFail() {
        accountService.login("yauritux");
        assertThrows(DomainException.class, () -> accountService.deposit(BigDecimal.valueOf(-200)));
    }

    @Test
    void depositAddAmountToBalance() {
        accountService.login("yauritux");
        accountService.deposit(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), accountService.getCurrentAccount().getBalance());
    }

    @Test
    void withdrawWithoutLoginShouldFail() {
        assertThrows(DomainException.class, () -> accountService.withdraw(BigDecimal.valueOf(50)));
    }

    @Test
    void withdrawWithNegativeAmountShouldFail() {
        accountService.login("yauritux");
        assertThrows(DomainException.class, () -> accountService.withdraw(BigDecimal.valueOf(-50)));
    }

    @Test
    void withdrawWithInsufficientBalanceShouldFail() {
        accountService.login("yauritux");
        assertThrows(DomainException.class, () -> accountService.withdraw(BigDecimal.valueOf(50)));
    }

    @Test
    void withdrawShouldSubtractAmountFromBalance() {
        accountService.login("yauritux");
        accountService.deposit(BigDecimal.valueOf(100));
        TransactionResponse response = accountService.withdraw(BigDecimal.valueOf(30));
        assertEquals(BigDecimal.valueOf(70), response.getCustomerAccount().getBalance());
    }

    @Test
    void transferWithoutLoginShouldFail() {
        assertThrows(DomainException.class, () -> accountService.transfer("yauritux", BigDecimal.valueOf(50)));
    }

    @Test
    void transferToSelfShouldFail() {
        accountService.login("yauritux");
        accountService.deposit(BigDecimal.valueOf(100));
        assertThrows(DomainException.class, () -> accountService.transfer("yauritux", BigDecimal.valueOf(50)));
    }

    @Test
    void transferWithInsufficientBalanceCreatesDebt() {
        accountService.login("yauritux");
        accountService.deposit(BigDecimal.valueOf(80));
        TransactionResponse response = accountService.transfer("Alice", BigDecimal.valueOf(100));

        assertEquals(BigDecimal.ZERO, response.getCustomerAccount().getBalance());
        assertEquals(BigDecimal.valueOf(80), response.getTransferList().get(0).transferAmount());
        assertEquals(1, response.getDebtAccounts().size());
        assertEquals(BigDecimal.valueOf(20), response.getDebtAccounts().get(0).getAmount());
    }

    @Test
    void depositAutoSettlesDebt() {
        accountService.login("yauritux");
        accountService.deposit(BigDecimal.valueOf(80));
        accountService.transfer("Alice", BigDecimal.valueOf(100));
        accountService.logout();

        accountService.login("yauritux");
        TransactionResponse response = accountService.deposit(BigDecimal.valueOf(30));

        assertEquals(BigDecimal.valueOf(10), response.getCustomerAccount().getBalance());
        assertTrue(response.getDebtAccounts().isEmpty());
        assertEquals(1, response.getTransferList().size());
    }
}
