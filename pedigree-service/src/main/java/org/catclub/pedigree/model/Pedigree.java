package org.catclub.pedigree.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "pedigrees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedigree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cat_id", nullable = false)
    private Long catId;

    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "mother_pedigree_id")
    private Long motherPedigreeId;

    @Column(name = "father_pedigree_id")
    private Long fatherPedigreeId;
}