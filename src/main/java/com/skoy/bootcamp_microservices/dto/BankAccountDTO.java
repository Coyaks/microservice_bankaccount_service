package com.skoy.bootcamp_microservices.dto;

import com.skoy.bootcamp_microservices.enums.AccountTypeEnum;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {
    private String id;
    private String customerId;
    private AccountTypeEnum accountType;
    private String accountNumber;
    private BigDecimal balance;
}
