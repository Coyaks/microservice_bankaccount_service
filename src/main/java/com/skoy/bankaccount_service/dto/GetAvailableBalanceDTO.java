package com.skoy.bankaccount_service.dto;

import com.skoy.bankaccount_service.enums.AccountTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAvailableBalanceDTO {
    private String customerId;
    private AccountTypeEnum accountType;
}