package com.dkatalis.domain.aggregate;

import com.dkatalis.domain.entity.CustomerAccount;
import com.dkatalis.domain.entity.DebtAccount;
import com.dkatalis.domain.valueobject.CustomerStatus;
import com.dkatalis.domain.valueobject.TransactionResponse;
import com.dkatalis.domain.valueobject.TransferDto;
import com.dkatalis.port.in.CustomerAccountServicePort;
import com.dkatalis.port.out.CustomerAccountRepositoryPort;
import com.dkatalis.port.out.DebtAccountRepositoryPort;
import com.dkatalis.sharedkernel.DomainException;

import java.math.BigDecimal;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public class AccountAggregate implements CustomerAccountServicePort {

    private final CustomerAccountRepositoryPort accountRepository;
    private final DebtAccountRepositoryPort<String> debtRepository;

    private CustomerAccount currentAccount;

    public AccountAggregate(
            CustomerAccountRepositoryPort accountRepository,
            DebtAccountRepositoryPort<String> debtRepository) {
        this.accountRepository = accountRepository;
        this.debtRepository = debtRepository;
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
    public TransactionResponse deposit(BigDecimal depositAmount) {
        validateLogin();
        validatePositiveAmount(depositAmount);

        currentAccount.setBalance(currentAccount.getBalance().add(depositAmount));
        accountRepository.save(currentAccount);

        TransactionResponse response = new TransactionResponse();
        response.setCustomerAccount(currentAccount);

        List<DebtAccount> debts = debtRepository.findByDebtorAccount(currentAccount.getName());
        if (debts != null && !debts.isEmpty()) {
            List<DebtAccount> sorted = new ArrayList<>(debts);
            sorted.sort(Comparator.comparing(DebtAccount::getCreditorAccountName));

            for (DebtAccount debt : sorted) {
                if (currentAccount.getBalance().compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }

                BigDecimal debtAmount = debt.getAmount();
                BigDecimal pay = currentAccount.getBalance().min(debtAmount);

                if (pay.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                CustomerAccount creditor = findOrCreateAccount(debt.getCreditorAccountName());
                creditor.setBalance(creditor.getBalance().add(pay));
                accountRepository.save(creditor);

                currentAccount.setBalance(currentAccount.getBalance().subtract(pay));
                accountRepository.save(currentAccount);

                BigDecimal remainingDebt = debtAmount.subtract(pay);
                if (remainingDebt.compareTo(BigDecimal.ZERO) == 0) {
                    debtRepository.remove(debt);
                } else {
                    debt.setAmount(remainingDebt);
                }

                response.addTransfer(new TransferDto(creditor, pay));
            }
        }

        response.setDebtAccounts(new ArrayList<>(debtRepository.findByDebtorAccount(currentAccount.getName())));
        return response;
    }

    @Override
    public TransactionResponse withdraw(BigDecimal withdrawAmount) {
        validateLogin();
        validatePositiveAmount(withdrawAmount);

        if (withdrawAmount.compareTo(currentAccount.getBalance()) > 0) {
            throw new DomainException(String.format(
                    "Insufficient balance. Your current balance is $%s", currentAccount.getBalance()));
        }

        currentAccount.setBalance(currentAccount.getBalance().subtract(withdrawAmount));
        accountRepository.save(currentAccount);

        TransactionResponse response = new TransactionResponse();
        response.setCustomerAccount(currentAccount);
        return response;
    }

    @Override
    public TransactionResponse transfer(String targetName, BigDecimal transferAmount) {
        validateLogin();
        validatePositiveAmount(transferAmount);

        if (currentAccount.getName().equals(targetName)) {
            throw new DomainException("Cannot transfer to yourself");
        }

        CustomerAccount target = findOrCreateAccount(targetName);

        TransactionResponse response = new TransactionResponse();
        response.setCustomerAccount(currentAccount);

        // If the target already owes the current account, settle that debt first.
        List<DebtAccount> targetDebts = debtRepository.findByDebtorAccount(target.getName());
        if (targetDebts != null && !targetDebts.isEmpty()) {
            for (DebtAccount debt : targetDebts) {
                if (debt.getCreditorAccountName().equalsIgnoreCase(currentAccount.getName())) {
                    BigDecimal payFromDebt = debt.getAmount().min(transferAmount);
                    BigDecimal remainingDebt = debt.getAmount().subtract(payFromDebt);

                    if (remainingDebt.compareTo(BigDecimal.ZERO) == 0) {
                        debtRepository.remove(debt);
                    } else {
                        debt.setAmount(remainingDebt);
                    }

                    transferAmount = transferAmount.subtract(payFromDebt);

                    if (transferAmount.compareTo(BigDecimal.ZERO) == 0) {
                        response.setDebtAccounts(new ArrayList<>(debtRepository.findByDebtorAccount(currentAccount.getName())));
                        return response;
                    }
                    break;
                }
            }
        }

        // Transfer any remaining amount as cash.
        BigDecimal available = currentAccount.getBalance();
        BigDecimal actual = available.min(transferAmount);

        if (actual.compareTo(BigDecimal.ZERO) > 0) {
            currentAccount.setBalance(currentAccount.getBalance().subtract(actual));
            target.setBalance(target.getBalance().add(actual));
            accountRepository.save(currentAccount);
            accountRepository.save(target);
            response.addTransfer(new TransferDto(target, actual));
        }

        BigDecimal remaining = transferAmount.subtract(actual);
        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            DebtAccount debt = new DebtAccount(currentAccount.getName(), target.getName(), remaining);
            debtRepository.save(debt);
            response.addDebtAccount(debt);
        }

        response.setDebtAccounts(new ArrayList<>(debtRepository.findByDebtorAccount(currentAccount.getName())));
        return response;
    }

    @Override
    public CustomerStatus getCurrentStatus() {
        validateLogin();

        List<DebtAccount> owedTo = new ArrayList<>(debtRepository.findByDebtorAccount(currentAccount.getName()));
        List<DebtAccount> owedFrom = new ArrayList<>(debtRepository.findByCreditorAccount(currentAccount.getName()));
        owedTo.sort(Comparator.comparing(DebtAccount::getCreditorAccountName));
        owedFrom.sort(Comparator.comparing(DebtAccount::getDebtorAccountName));

        return new CustomerStatus(currentAccount, owedTo, owedFrom);
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

    private CustomerAccount findOrCreateAccount(String name) {
        return accountRepository.findByCustomerName(name).orElseGet(() -> {
            CustomerAccount newAccount = new CustomerAccount(name);
            accountRepository.save(newAccount);
            return newAccount;
        });
    }
}
