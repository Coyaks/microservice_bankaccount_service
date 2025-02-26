package com.skoy.bootcamp_microservices.enums;

import java.util.Objects;

public enum AccountTypeEnumFull {
    AHORRO("AHORRO", "AHORRO"),
    CORRIENTE("CORRIENTE", "CORRIENTE"),
    PLAZO_FIJO("PLAZO_FIJO", "PLAZO FIJO");

    private final String id;
    private final String name;

    AccountTypeEnumFull(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static AccountTypeEnumFull fromCode(String id) {
        for (AccountTypeEnumFull type : AccountTypeEnumFull.values()) {
            if (Objects.equals(type.getId(), id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Código no válido: " + id);
    }
}
