package com.skoy.bankaccount_service.enums;

public enum TransactionTypeEnum {
    DEPOSIT("Deposito"),
    WITHDRAWAL("Retiro"),
    TRANSFER("Transferencia");

    private final String name;

    TransactionTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}