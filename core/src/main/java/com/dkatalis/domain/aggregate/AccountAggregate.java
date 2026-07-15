package com.dkatalis.domain.aggregate;

import com.dkatalis.domain.entity.CustomerAccount;
import com.dkatalis.port.in.CustomerAccountServicePort;
import com.dkatalis.port.out.CustomerAccountRepositoryPort;
import com.dkatalis.sharedkernel.DomainException;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public class AccountAggregate implements CustomerAccountServicePort {

    private final CustomerAccountRepositoryPort accountRepository;

    private CustomerAccount currentAccount;

    public AccountAggregate(CustomerAccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public BigDecimal login(String name) {
        Optional<CustomerAccount> existingCustomer = accountRepository.findByCustomerName(name);
        if (existingCustomer.isPresent()) {
            currentAccount = existingCustomer.get();
        } else {
            currentAccount = new CustomerAccount(name);
        }
        return currentAccount.getBalance();
    }

    @Override
    public void logout() {
        currentAccount = null;
    }

    @Override
    public CustomerAccount getCurrentAccount() {
        return currentAccount;
    }

    @Override
    public BigDecimal deposit(BigDecimal amount) {
        if (currentAccount == null) {
            throw new DomainException("Please login first!");
        }
        return currentAccount.getBalance().add(amount);
    }
}
