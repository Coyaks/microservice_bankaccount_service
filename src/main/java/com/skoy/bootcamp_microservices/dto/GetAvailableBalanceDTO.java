package com.skoy.bootcamp_microservices.dto;

import com.skoy.bootcamp_microservices.enums.AccountTypeEnum;
import com.skoy.bootcamp_microservices.enums.TransactionTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GetAvailableBalanceDTO {
    private String customerId;
    private AccountTypeEnum accountType;
}