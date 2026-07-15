package com.dkatalis.domain.entity;

import com.dkatalis.sharedkernel.DomainException;

import java.math.BigDecimal;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public class DebtAccount {

    private String debtorAccountName;
    private String creditorAccountName;
    private BigDecimal amount;

    public DebtAccount() {}

    public DebtAccount(String debtorAccountName, String creditorAccountName, BigDecimal amount) {
        if (debtorAccountName == null || debtorAccountName.isBlank()) {
            throw new DomainException("Debtor account name is required!");
        }
        if (creditorAccountName == null || creditorAccountName.isBlank()) {
            throw new DomainException("Creditor account name is required!");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Amount should not be less than or equal to zero!");
        }
        this.debtorAccountName = debtorAccountName;
        this.creditorAccountName = creditorAccountName;
        this.amount = amount;
    }

    public String getDebtorAccountName() {
        return debtorAccountName;
    }

    public void setDebtorAccountName(String debtorAccountName) {
        this.debtorAccountName = debtorAccountName;
    }

    public String getCreditorAccountName() {
        return creditorAccountName;
    }

    public void setCreditorAccountName(String creditorAccountName) {
        this.creditorAccountName = creditorAccountName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
