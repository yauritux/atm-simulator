package com.dkatalis.domain.entity;

import com.dkatalis.sharedkernel.DomainException;

import java.math.BigDecimal;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public class LedgerLog {

    private String senderAccountName;
    private String receiverAccountName;
    private BigDecimal amount;

    public LedgerLog() {}

    public LedgerLog(String senderAccountName, String receiverAccountName, BigDecimal amount) {
        if (senderAccountName == null || senderAccountName.isBlank()) {
            throw new DomainException("Sender account name is required!");
        }
        if (receiverAccountName == null || receiverAccountName.isBlank()) {
            throw new DomainException("Receiver account name is required!");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Amount should not be less than or equal to zero!");
        }
        this.senderAccountName = senderAccountName;
        this.receiverAccountName = receiverAccountName;
        this.amount = amount;
    }

    public String getSenderAccountName() {
        return senderAccountName;
    }

    public void setSenderAccountName(String senderAccountName) {
        this.senderAccountName = senderAccountName;
    }

    public String getReceiverAccountName() {
        return receiverAccountName;
    }

    public void setReceiverAccountName(String receiverAccountName) {
        this.receiverAccountName = receiverAccountName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
