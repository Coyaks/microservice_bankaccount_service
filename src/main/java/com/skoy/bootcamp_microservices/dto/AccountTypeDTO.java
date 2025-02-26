package com.skoy.bootcamp_microservices.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTypeDTO {
    private String id;

    public AccountTypeDTO() {
    }

    public AccountTypeDTO(String id) {
        this.id = id;
    }
}
