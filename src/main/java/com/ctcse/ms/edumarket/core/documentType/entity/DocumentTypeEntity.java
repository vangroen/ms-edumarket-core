package com.ctcse.ms.edumarket.core.documentType.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTypeEntity {

    @Id
    @GeneratedValue
    @Column(name = "idDocumentType")
    private Long id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;
}
