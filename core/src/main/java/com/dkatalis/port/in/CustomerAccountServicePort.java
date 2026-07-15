package com.dkatalis.port.in;

import com.dkatalis.domain.entity.CustomerAccount;
import com.dkatalis.domain.valueobject.TransactionResponse;

import java.math.BigDecimal;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public interface CustomerAccountServicePort {

    BigDecimal login(String name);

    void logout();

    CustomerAccount getCurrentAccount();

    TransactionResponse deposit(BigDecimal depositAmount);

    TransactionResponse withdraw(BigDecimal withdrawAmount);

    TransactionResponse transfer(String targetName, BigDecimal transferAmount);
}
