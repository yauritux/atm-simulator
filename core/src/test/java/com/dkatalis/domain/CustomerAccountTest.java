package com.dkatalis.domain;

import com.dkatalis.domain.entity.CustomerAccount;
import com.dkatalis.sharedkernel.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerAccountTest {

    @Test
    void newCustomerAccountWithNullNameShouldFail() {
        assertThrows(DomainException.class, () -> new CustomerAccount(null));
    }

    @Test
    void newCustomerAccountWithEmptyNameShouldFail() {
        assertThrows(DomainException.class, () -> new CustomerAccount(""));
    }

    @Test
    void newCustomerAccountWithValidNameShouldSucceed() {
        assertDoesNotThrow(() -> new CustomerAccount("yauritux"));
    }

    @Test
    void newCustomerAccountNameShouldBeSet() {
        var customerAccount = new CustomerAccount("yauritux");
        assertEquals("yauritux", customerAccount.getName());
    }

    @Test
    void newCustomerAccountWithoutDepositGotZeroBalance() {
        var customerAccount = new CustomerAccount("yauritux");
        assertEquals(BigDecimal.ZERO, customerAccount.getBalance());
    }

    @Test
    void newCustomerAccountWithInitialBalanceShouldSucceed() {
        var customerAccount = new CustomerAccount("yauritux", BigDecimal.valueOf(500_000));
        assertEquals(BigDecimal.valueOf(500_000), customerAccount.getBalance());
    }

    @Test
    void newCustomerAccountWithInitialBalanceAndEmptyNameShouldFail() {
        assertThrows(DomainException.class, () -> new CustomerAccount("", BigDecimal.valueOf(1_000_000)));
    }
}
