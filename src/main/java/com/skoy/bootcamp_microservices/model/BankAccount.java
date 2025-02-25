package com.skoy.bootcamp_microservices.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "bank_accounts")
public class BankAccount {
    @Id
    private String id;
    private String customerId;
    private String accountType; // AHORRO, CORRIENTE, PLAZO_FIJO
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}