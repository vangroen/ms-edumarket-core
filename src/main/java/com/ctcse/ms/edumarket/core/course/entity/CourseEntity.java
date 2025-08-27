package com.ctcse.ms.edumarket.core.course.entity;

import com.ctcse.ms.edumarket.core.courseType.entity.CourseTypeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCourse")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idCourseType", nullable = false)
    private CourseTypeEntity courseType;
}
