package com.ctcse.ms.edumarket.core.student.dto;

import com.ctcse.ms.edumarket.core.academicRank.dto.AcademicRankDto;
import com.ctcse.ms.edumarket.core.institution.dto.InstitutionDto;
import com.ctcse.ms.edumarket.core.person.dto.PersonDto;
import com.ctcse.ms.edumarket.core.profession.dto.ProfessionDto;
import lombok.Data;

@Data
public class StudentDto {

    private Long id;
    private ProfessionDto profession;
    private InstitutionDto institution;
    private AcademicRankDto academicRank;
    private PersonDto person;
}
