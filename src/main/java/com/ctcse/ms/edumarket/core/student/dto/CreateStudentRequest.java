package com.ctcse.ms.edumarket.core.student.dto;

import lombok.Data;

@Data
public class CreateStudentRequest {

    private Long idProfession;
    private Long idInstitution;
    private Long idAcademicRank;
    private Long idPerson;
}
