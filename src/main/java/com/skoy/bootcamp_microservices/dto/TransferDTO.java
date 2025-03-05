package com.skoy.bootcamp_microservices.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferDTO {
    private String sourceAccountId;
    private String destinationAccountId;
    private BigDecimal amount;
}
