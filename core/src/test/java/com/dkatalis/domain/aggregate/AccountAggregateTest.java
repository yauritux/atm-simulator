package com.dkatalis.domain.aggregate;

import com.dkatalis.domain.entity.CustomerAccount;
import com.dkatalis.domain.port.out.FakeCustomerAccountRepository;
import com.dkatalis.sharedkernel.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountAggregateTest {

    private AccountAggregate accountService;
    private FakeCustomerAccountRepository accountRepository;


    @BeforeEach
    void setUp() {
        accountRepository = new FakeCustomerAccountRepository();
        accountService = new AccountAggregate(accountRepository);
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
        BigDecimal balance = accountService.withdraw(BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), balance);
    }
}
