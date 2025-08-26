package com.ctcse.ms.edumarket.core.person.dto;

import com.ctcse.ms.edumarket.core.documentType.dto.DocumentTypeDto;
import lombok.Data;

@Data
public class PersonDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String documentNumber;
    private DocumentTypeDto documentType;
}
