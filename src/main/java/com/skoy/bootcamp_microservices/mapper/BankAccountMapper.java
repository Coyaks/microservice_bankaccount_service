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
        account.setAvailableBalance(dto.getAvailableBalance());
        account.setOwners(dto.getOwners());
        account.setAuthorizedSigners(dto.getAuthorizedSigners());
        account.setMaintenanceCommission(dto.getMaintenanceCommission());
        //account.setMaxFreeTransactions(10);
        return account;
    }


    public static BankAccountDTO toDto(BankAccount item) {
        BankAccountDTO dto = new BankAccountDTO();
        dto.setId(item.getId());
        dto.setCustomerId(item.getCustomerId());
        dto.setAccountType(item.getAccountType());
        dto.setAccountNumber(item.getAccountNumber());
        dto.setAvailableBalance(item.getAvailableBalance());
        dto.setOwners(item.getOwners());
        dto.setAuthorizedSigners(item.getAuthorizedSigners());
        dto.setMaintenanceCommission(item.getMaintenanceCommission());
        return dto;
    }


}
