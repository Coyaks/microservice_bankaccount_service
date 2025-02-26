package com.skoy.bootcamp_microservices.mapper;

import com.skoy.bootcamp_microservices.dto.BankAccountDTO;
import com.skoy.bootcamp_microservices.model.BankAccount;

public class BankAccountMapper {

    public static BankAccount toEntity(BankAccountDTO dto) {
        BankAccount account = new BankAccount();
        account.setId(dto.getId());
        account.setCustomerId(dto.getCustomerId());
        account.setAccountType(dto.getAccountType());
        account.setAccountNumber(dto.getAccountNumber());
        account.setBalance(dto.getBalance());
        return account;
    }


    public static BankAccountDTO toDto(BankAccount item) {
        BankAccountDTO dto = new BankAccountDTO();
        dto.setId(item.getId());
        dto.setCustomerId(item.getCustomerId());
        dto.setAccountType(item.getAccountType());
        dto.setAccountNumber(item.getAccountNumber());
        dto.setBalance(item.getBalance());
        return dto;
    }


}
