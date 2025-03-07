package com.skoy.bankaccount_service.dto;

import com.skoy.bankaccount_service.enums.CustomerTypeEnum;
import com.skoy.bankaccount_service.enums.DocumentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String id;
    private CustomerTypeEnum customerType;
    private String name;
    private String surname;
    private DocumentTypeEnum documentType;
    private String documentNumber;
    private String email;
    private String phone;
}
