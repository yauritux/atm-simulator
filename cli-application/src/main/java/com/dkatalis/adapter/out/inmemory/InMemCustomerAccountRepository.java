package com.dkatalis.adapter.out.inmemory;

import com.dkatalis.domain.entity.CustomerAccount;
import com.dkatalis.port.out.CustomerAccountRepositoryPort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public class InMemCustomerAccountRepository implements CustomerAccountRepositoryPort {

    private final Map<String, CustomerAccount> records = new HashMap<>();

    @Override
    public void save(CustomerAccount customerAccount) {
        records.putIfAbsent(customerAccount.getName(), customerAccount);
    }

    @Override
    public Optional<CustomerAccount> findByCustomerName(String name) {
        return Optional.ofNullable(records.get(name));
    }
}
