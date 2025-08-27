package com.ctcse.ms.edumarket.core.course.dto;

import com.ctcse.ms.edumarket.core.common.validation.NotBlankWithMessage;
import com.ctcse.ms.edumarket.core.common.validation.SizeWithMessage;
import lombok.Data;

@Data
public class CreateCourseRequest {

    @NotBlankWithMessage(fieldName = "nombre")
    @SizeWithMessage(fieldName = "nombre", min = 3, max = 100)
    private String name;

    @NotBlankWithMessage(fieldName = "id del tipo de curso")
    @SizeWithMessage(fieldName = "id del tipo de curso", min = 2, max = 100)
    private Long idCourseType;

    @NotBlankWithMessage(fieldName = "id de la modalidad")
    @SizeWithMessage(fieldName = "id del tipo de curso", min = 2, max = 100)
    private Long idModality;
}
