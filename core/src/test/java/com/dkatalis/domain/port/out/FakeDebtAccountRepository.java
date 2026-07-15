package com.dkatalis.domain.port.out;

import com.dkatalis.domain.entity.DebtAccount;
import com.dkatalis.port.out.DebtAccountRepositoryPort;

import java.util.*;

/**
 * @author yauritux
 * @version 1.0
 */
public class FakeDebtAccountRepository implements DebtAccountRepositoryPort<String> {

    private final Map<String, List<DebtAccount>> debtorRecords = new HashMap<>();

    @Override
    public void save(DebtAccount debtAccount) {
        if (debtAccount == null || debtAccount.getDebtorAccountName() == null || debtAccount.getDebtorAccountName().isBlank()) {
            return;
        }

        List<DebtAccount> records = debtorRecords.computeIfAbsent(debtAccount.getDebtorAccountName(), k -> new ArrayList<>());
        for (DebtAccount existing : records) {
            if (existing.getCreditorAccountName().equals(debtAccount.getCreditorAccountName())) {
                existing.setAmount(existing.getAmount().add(debtAccount.getAmount()));
                return;
            }
        }
        records.add(debtAccount);
    }

    @Override
    public List<DebtAccount> findByDebtorAccount(String debtorAccountId) {
        return new ArrayList<>(debtorRecords.getOrDefault(debtorAccountId, Collections.emptyList()));
    }

    @Override
    public List<DebtAccount> findByCreditorAccount(String creditorAccountId) {
        List<DebtAccount> result = new ArrayList<>();
        for (List<DebtAccount> debts : debtorRecords.values()) {
            for (DebtAccount debt : debts) {
                if (debt.getCreditorAccountName().equals(creditorAccountId)) {
                    result.add(debt);
                }
            }
        }
        return result;
    }

    @Override
    public void remove(DebtAccount debtAccount) {
        List<DebtAccount> records = debtorRecords.get(debtAccount.getDebtorAccountName());
        if (records == null) {
            return;
        }
        records.removeIf(da -> da.getCreditorAccountName().equals(debtAccount.getCreditorAccountName()));
        if (records.isEmpty()) {
            debtorRecords.remove(debtAccount.getDebtorAccountName());
        }
    }
}
