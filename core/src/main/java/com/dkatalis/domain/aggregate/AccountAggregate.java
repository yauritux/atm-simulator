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
        currentAccount = existingCustomer.orElseGet(() -> new CustomerAccount(name));
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
    public BigDecimal deposit(BigDecimal depositAmount) {
        validateLogin();
        validatePositiveAmount(depositAmount);

        currentAccount.setBalance(currentAccount.getBalance().add(depositAmount));
        accountRepository.save(currentAccount);

        return currentAccount.getBalance();
    }

    @Override
    public BigDecimal withdraw(BigDecimal withdrawAmount) {
        validateLogin();
        validatePositiveAmount(withdrawAmount);

        if (withdrawAmount.compareTo(currentAccount.getBalance()) > 0) {
            throw new DomainException(String.format(
                    "Insufficient balance. Your current balance is $%s",
                    currentAccount.getBalance()
            ));
        }

        currentAccount.setBalance(currentAccount.getBalance().subtract(withdrawAmount));
        accountRepository.save(currentAccount);

        return currentAccount.getBalance();
    }

    private void validateLogin() {
        if (currentAccount == null) {
            throw new DomainException("Please login first!");
        }
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Amount must be positive");
        }
    }
}
