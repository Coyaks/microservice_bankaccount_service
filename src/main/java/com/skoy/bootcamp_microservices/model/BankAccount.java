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
    private AccountTypeEnum accountType; // AHORRO, CORRIENTE, PLAZO_FIJO
    private String accountNumber;
    private BigDecimal balance;
    private List<String> owners; // IDs de titulares (obligatorio: al menos 1)
    private List<String> authorizedSigners; // IDs de firmantes (opcional)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}