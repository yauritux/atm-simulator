package com.dkatalis.domain.entity;

import com.dkatalis.sharedkernel.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public class LedgerLogTest {

    @Test
    void newLedgerLogWithNullSenderAccountNameShouldFail() {
        assertThrows(DomainException.class, () ->
                new LedgerLog(null,
                        "yauritux", BigDecimal.valueOf(100)));
    }

    @Test
    void newLedgerLogWithEmptySenderAccountNameShouldFail() {
        assertThrows(DomainException.class, () ->
                new LedgerLog("",
                        "yauritux", BigDecimal.valueOf(100)));
    }

    @Test
    void newLedgerLogWithNullReceiverAccountNameShouldFail() {
        assertThrows(DomainException.class, () ->
                new LedgerLog("yauritux",
                        null, BigDecimal.valueOf(100)));
    }

    @Test
    void newLedgerLogWithEmptyReceiverAccountNameShouldFail() {
        assertThrows(DomainException.class, () ->
                new LedgerLog("yauritux",
                        "", BigDecimal.valueOf(100)));
    }

    @Test
    void newLedgerLogWithZeroAmountShouldFail() {
        assertThrows(DomainException.class, () ->
                new LedgerLog("yauritux",
                        "alice", BigDecimal.ZERO));
    }

    @Test
    void newLedgerLogWithNegativeAmountShouldFail() {
        assertThrows(DomainException.class, () ->
                new LedgerLog("yauritux",
                        "alice", BigDecimal.valueOf(-100)));
    }

    @Test
    void newLedgerLogWithValidParametersShouldSucceed() {
        assertDoesNotThrow(() ->
                new LedgerLog("yauritux",
                        "alice", BigDecimal.valueOf(100)));
    }

    @Test
    void updateSenderAccountName() {
        var sender = new LedgerLog("yauritux", "alice", BigDecimal.valueOf(100));
        sender.setAmount(BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), sender.getAmount());
    }
}
