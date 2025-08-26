package com.ctcse.ms.edumarket.core.courseType.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class CourseTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCourseType")
    private Long id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;
}
