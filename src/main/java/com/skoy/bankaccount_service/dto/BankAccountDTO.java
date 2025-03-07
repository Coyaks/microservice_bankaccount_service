package com.skoy.bankaccount_service.dto;

import com.skoy.bankaccount_service.enums.AccountTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {
    private String id;
    private String customerId;
    private AccountTypeEnum accountType;
    private String accountNumber;
    private BigDecimal availableBalance;
    private List<String> owners;
    private List<String> authorizedSigners;
    private BigDecimal maintenanceCommission;

    private int maxFreeTransactions;
    private BigDecimal transactionCommission;
    private int transactionCount;
}
