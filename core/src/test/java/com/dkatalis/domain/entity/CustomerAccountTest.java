package com.dkatalis.domain.entity;

import com.dkatalis.sharedkernel.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
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

    @Test
    void twoCustomersWithSameNameShouldBeEqual() {
        var customer1 = new CustomerAccount("yauritux");
        var customer2 = new CustomerAccount("yauritux", BigDecimal.valueOf(1_500_000));
        assertEquals(customer1, customer2);
        assertEquals(customer1.hashCode(), customer2.hashCode());
    }
}
