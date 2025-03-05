package com.skoy.bootcamp_microservices.dto;

import com.skoy.bootcamp_microservices.enums.AccountTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAvailableBalanceDTO {
    private String customerId;
    private AccountTypeEnum accountType;
}