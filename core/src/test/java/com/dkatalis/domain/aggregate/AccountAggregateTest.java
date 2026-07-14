package com.dkatalis.domain.aggregate;

import com.dkatalis.domain.port.out.FakeCustomerAccountRepository;
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
    void logoutShouldClearCurrentAccount() {
        accountService.login("yauritux");
        assertNotNull(accountService.getCurrentAccount());
        accountService.logout();
        assertNull(accountService.getCurrentAccount());
    }
}
