package com.dkatalis.domain.valueobject;

import com.dkatalis.domain.entity.CustomerAccount;
import com.dkatalis.domain.entity.DebtAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 */
public class TransactionResponse {

    private CustomerAccount customerAccount;
    private List<TransferDto> transferList = new ArrayList<>();
    private List<DebtAccount> debtAccounts = new ArrayList<>();

    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount customerAccount) {
        this.customerAccount = customerAccount;
    }

    public List<TransferDto> getTransferList() {
        return transferList;
    }

    public void setTransferList(List<TransferDto> transferList) {
        this.transferList = transferList;
    }

    public List<DebtAccount> getDebtAccounts() {
        return debtAccounts;
    }

    public void setDebtAccounts(List<DebtAccount> debtAccounts) {
        this.debtAccounts = debtAccounts;
    }

    public void addTransfer(TransferDto transferDto) {
        this.transferList.add(transferDto);
    }

    public void addDebtAccount(DebtAccount debtAccount) {
        this.debtAccounts.add(debtAccount);
    }

}
