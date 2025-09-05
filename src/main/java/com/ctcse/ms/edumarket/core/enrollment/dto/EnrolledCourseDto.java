package com.ctcse.ms.edumarket.core.enrollment.dto;

import com.ctcse.ms.edumarket.core.courseType.dto.CourseTypeDto;
import com.ctcse.ms.edumarket.core.modality.dto.ModalityDto;
import lombok.Data;

@Data
public class EnrolledCourseDto {
    private Long id;
    private String name;
    private CourseTypeDto courseType;
    private ModalityDto modality;
}
