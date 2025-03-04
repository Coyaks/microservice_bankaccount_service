package com.skoy.bootcamp_microservices.dto;

import com.skoy.bootcamp_microservices.enums.AccountTypeEnum;
import lombok.*;

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
}
