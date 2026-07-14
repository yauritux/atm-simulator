package com.dkatalis.port.out;

import com.dkatalis.domain.entity.CustomerAccount;

import java.util.Optional;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public interface CustomerAccountRepositoryPort {

    void save(CustomerAccount customerAccount);

    Optional<CustomerAccount> findByCustomerName(String name);
}
