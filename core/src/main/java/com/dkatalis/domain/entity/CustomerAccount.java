package com.dkatalis.domain.entity;

import com.dkatalis.sharedkernel.DomainException;

import java.math.BigDecimal;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public class CustomerAccount {

    private String name;
    private BigDecimal balance;

    public CustomerAccount(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Customer account name must not be empty!");
        }
        this.name = name;
        this.balance = BigDecimal.ZERO;
    }

    public CustomerAccount(String name, BigDecimal balance) {
        this(name);
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
