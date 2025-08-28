package com.ctcse.ms.edumarket.core.course.dto;

import com.ctcse.ms.edumarket.core.common.validation.NotBlankWithMessage;
import com.ctcse.ms.edumarket.core.common.validation.SizeWithMessage;
import lombok.Data;

@Data
public class CreateCourseRequest {

    @NotBlankWithMessage(fieldName = "nombre")
    @SizeWithMessage(fieldName = "nombre", min = 3, max = 100)
    private String name;

    private Long idCourseType;

    private Long idModality;

    private Long idInstitution;
}
