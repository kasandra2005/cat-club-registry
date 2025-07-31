package org.catclub.cat.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "cats")
@Data
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String breed;

    @Column(nullable = false, length = 30)
    private String color;

    @Column(nullable = false)
    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "mother_id")
    private Long motherId;

    @Column(name = "father_id")
    private Long fatherId;

    @Column(length = 500)
    private String description;

    @CreationTimestamp
    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDate registrationDate;
}