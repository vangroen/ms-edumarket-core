package com.ctcse.ms.edumarket.core.course.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseInstitutionId implements Serializable {

    @Column(name = "idCourse")
    private Long courseId;

    @Column(name = "idInstitution")
    private Long institutionId;
}