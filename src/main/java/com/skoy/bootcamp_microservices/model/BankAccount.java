package com.skoy.bootcamp_microservices.model;

import com.skoy.bootcamp_microservices.enums.AccountTypeEnum;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "bank_accounts")
public class BankAccount {
    @Id
    private String id;
    private String customerId;
    private AccountTypeEnum accountType;
    private String accountNumber;
    private BigDecimal availableBalance;
    private List<String> owners;
    private List<String> authorizedSigners; // (optional)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}