package com.dkatalis.port.in;

import com.dkatalis.domain.entity.CustomerAccount;

import java.math.BigDecimal;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public interface CustomerAccountServicePort {

    BigDecimal login(String name);

    void logout();

    CustomerAccount getCurrentAccount();
}
