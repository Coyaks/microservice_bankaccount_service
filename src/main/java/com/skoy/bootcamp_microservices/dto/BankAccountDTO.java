package com.skoy.bootcamp_microservices.dto;

import com.skoy.bootcamp_microservices.model.BankAccount;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {
    private String id;
    private String customerId;
    private String accountType;
    private BigDecimal balance;

    public BankAccount toEntity() {
        BankAccount account = new BankAccount();
        account.setId(this.id);
        account.setCustomerId(this.customerId);
        account.setAccountType(this.accountType);
        account.setBalance(this.balance);
        return account;
    }


    public static BankAccountDTO fromEntity(BankAccount account) {
        BankAccountDTO dto = new BankAccountDTO();
        dto.setId(account.getId());
        dto.setCustomerId(account.getCustomerId());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        return dto;
    }

}
