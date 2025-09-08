package com.ctcse.ms.edumarket.core.person.entity;

import com.ctcse.ms.edumarket.core.documentType.entity.DocumentTypeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPerson")
    private Long id;

    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "documentNumber", length = 16, unique = true)
    private String documentNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idDocumentType", nullable = false)
    private DocumentTypeEntity documentType;
}
