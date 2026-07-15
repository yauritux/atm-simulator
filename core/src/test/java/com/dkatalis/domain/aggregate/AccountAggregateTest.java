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
        var account = new CustomerAccount("yauritux", BigDecimal.valueOf(500_000));
        accountRepository.save(account);

        BigDecimal balance = accountService.login("yauritux");
        assertEquals(BigDecimal.valueOf(500_000), balance);
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
        assertThrows(DomainException.class, () -> accountService.deposit(BigDecimal.valueOf(100_000)));
    }

    @Test
    void depositAddAmountToBalance() {
        accountService.login("yauritux");
        BigDecimal balance = accountService.deposit(BigDecimal.valueOf(100_000));
        assertEquals(BigDecimal.valueOf(100_000), balance);
    }
}
