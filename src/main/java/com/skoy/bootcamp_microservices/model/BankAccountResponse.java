package com.skoy.bootcamp_microservices.model;

import java.math.BigDecimal;

public class BankAccountResponse {
    private BankAccount bankAccount;
    private BigDecimal commissionAmount;

    public BankAccountResponse(BankAccount bankAccount, BigDecimal commissionAmount) {
        this.bankAccount = bankAccount;
        this.commissionAmount = commissionAmount;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }
}