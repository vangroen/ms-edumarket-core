package com.ctcse.ms.edumarket.core.student.dto;

import com.ctcse.ms.edumarket.core.common.validation.NotBlankWithMessage;
import com.ctcse.ms.edumarket.core.common.validation.SizeWithMessage;
import lombok.Data;

@Data
public class CreateStudentRequest {

    @NotBlankWithMessage(fieldName = "id de la profesion")
    @SizeWithMessage(fieldName = "id de la profesion", min = 1, max = 100)
    private Long idProfession;

    @NotBlankWithMessage(fieldName = "id de la institucion")
    @SizeWithMessage(fieldName = "id de la institucion", min = 1, max = 100)
    private Long idInstitution;

    @NotBlankWithMessage(fieldName = "id del rango académico")
    @SizeWithMessage(fieldName = "id del rango académico", min = 1, max = 100)
    private Long idAcademicRank;

    @NotBlankWithMessage(fieldName = "id de la persona")
    @SizeWithMessage(fieldName = "id de la persona", min = 1, max = 100)
    private Long idPerson;
}
