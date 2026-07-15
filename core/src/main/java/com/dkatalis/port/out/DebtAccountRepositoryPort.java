package com.dkatalis.port.out;

import com.dkatalis.domain.entity.DebtAccount;

import java.util.List;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public interface DebtAccountRepositoryPort<ID> {

    void save(DebtAccount debtAccount);

    List<DebtAccount> findByDebtorAccount(ID accountId);

    List<DebtAccount> findByCreditorAccount(ID accountId);

    void remove(DebtAccount debtAccount);
}
