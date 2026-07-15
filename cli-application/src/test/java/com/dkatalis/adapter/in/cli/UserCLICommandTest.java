package com.dkatalis.adapter.in.cli;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public class UserCLICommandTest {

    UserCLICommand sut = UserCLICommand.INSTANCE;

    @Test
    void login_nameIsNull_shouldFailWithMessage() {
        var response = sut.login(null);
        assertEquals("Usage: login [name]", response.get(0));
        assertEquals("E.g. : login Alice", response.get(1));
    }

    @Test
    void login_nameIsEmpty_shouldFailWithMessage() {
        var response = sut.login(" ");
        assertEquals("Usage: login [name]", response.get(0));
        assertEquals("E.g. : login Alice", response.get(1));
    }

    @Test
    void logout_noLogin_shouldFailWithMessage() {
        var response = sut.logout();
        assertEquals("You've logged-out already!%n", response.getFirst());
    }

    @Test
    void logout() {
        sut.login("Yauri");
        var response = sut.logout();
        assertEquals(String.format("%nGoodbye, %s!%n", "Yauri"), response.getFirst());
    }

    @Test
    void deposit_noAmount_shouldFailWithMessage() {
        sut.login("Yauri");
        var response = sut.deposit(null);
        assertEquals("Usage: deposit [amount]", response.get(0));
        assertEquals("E.g. : deposit 100", response.get(1));
    }

    @Test
    void deposit_withoutLogin_shouldFailWithMessage() {
        var response = sut.deposit(BigDecimal.TEN);
        assertEquals("Please login first!", response.getFirst());
    }

    @Test
    void deposit() {
        sut.login("Yauri");
        var response = sut.deposit(BigDecimal.TEN);
        assertEquals(String.format("%nYour balance is $%s%n", BigDecimal.TEN), response.getFirst());
    }
}
