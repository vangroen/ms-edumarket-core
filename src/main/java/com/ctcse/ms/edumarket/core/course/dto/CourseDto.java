package com.ctcse.ms.edumarket.core.course.dto;

import com.ctcse.ms.edumarket.core.courseType.dto.CourseTypeDto;
import lombok.Data;

@Data
public class CourseDto {

    private Long id;
    private String name;
    private CourseTypeDto courseType;
}
