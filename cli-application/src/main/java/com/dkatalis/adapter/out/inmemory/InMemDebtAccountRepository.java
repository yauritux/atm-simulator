package com.dkatalis.adapter.out.inmemory;

import com.dkatalis.domain.entity.DebtAccount;
import com.dkatalis.exception.ApplicationException;
import com.dkatalis.port.out.DebtAccountRepositoryPort;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public class InMemDebtAccountRepository implements DebtAccountRepositoryPort<String> {

    private final Map<String, List<DebtAccount>> debtorRecords = new HashMap<>();

    @Override
    public void save(DebtAccount debtAccount) {
        if (debtAccount == null) {
            throw new ApplicationException("debt account is missing!");
        }
        if (debtAccount.getDebtorAccountName() == null || debtAccount.getDebtorAccountName().trim().isEmpty()) {
            throw new ApplicationException("debtor name is missing!");
        }
        if (debtAccount.getCreditorAccountName() == null || debtAccount.getCreditorAccountName().trim().isEmpty()) {
            throw new ApplicationException("creditor name is missing!");
        }
        var existingRecord = debtorRecords.get(debtAccount.getDebtorAccountName());
        if (existingRecord == null || existingRecord.isEmpty()) {
            var daRecords = new ArrayList<DebtAccount>();
            daRecords.add(debtAccount);
            debtorRecords.put(debtAccount.getDebtorAccountName(), daRecords);
            return;
        }

        boolean newRecords = true;
        for (DebtAccount da : existingRecord) {
            if (da.getCreditorAccountName().equalsIgnoreCase(debtAccount.getCreditorAccountName())) {
                da.setAmount(da.getAmount().add(debtAccount.getAmount()));
                newRecords = false;
            }
        }
        if (newRecords) {
            existingRecord.add(debtAccount);
            debtorRecords.put(debtAccount.getDebtorAccountName(), existingRecord);
        }
    }

    @Override
    public List<DebtAccount> findByDebtorAccount(String debtorAccountId) {
        return debtorRecords.getOrDefault(debtorAccountId, Collections.emptyList());
    }

    @Override
    public List<DebtAccount> findByCreditorAccount(String creditorAccountId) {
        return debtorRecords.values().stream()
                .flatMap(List::stream)
                .filter(da -> da.getCreditorAccountName().equalsIgnoreCase(creditorAccountId))
                .collect(Collectors.toList());
    }

    @Override
    public void remove(DebtAccount debtAccount) {
        var existingRecord = debtorRecords.get(debtAccount.getDebtorAccountName());
        if (existingRecord == null) {
            return;
        }
        boolean foundIndex = false;
        int removedIndex = 0;
        for (int i = 0; i < existingRecord.size() ; i++) {
            if (existingRecord.get(i).getCreditorAccountName().equalsIgnoreCase(debtAccount.getCreditorAccountName())) {
                foundIndex = true;
                removedIndex = i;
                break;
            }
        }
        if (foundIndex) {
            existingRecord.remove(removedIndex);
            debtorRecords.put(debtAccount.getDebtorAccountName(), existingRecord);
        }
    }
}
