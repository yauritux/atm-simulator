package com.dkatalis.domain.valueobject;

import com.dkatalis.domain.entity.CustomerAccount;

import java.math.BigDecimal;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public record TransferDto(CustomerAccount targetAccount, BigDecimal transferAmount) {
}
