package com.dkatalis.domain.valueobject;

import com.dkatalis.domain.entity.CustomerAccount;
import com.dkatalis.domain.entity.DebtAccount;

import java.util.List;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public record CustomerStatus(
        CustomerAccount customerAccount, List<DebtAccount> owedTo, List<DebtAccount> owedFrom) {
}
