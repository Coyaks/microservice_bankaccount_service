package com.skoy.bankaccount_service.dto;

import com.skoy.bankaccount_service.enums.TransactionTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateBalanceDTO {
    private String productTypeId; // bankAccountId
    private TransactionTypeEnum transactionType;
    private BigDecimal amount;
}