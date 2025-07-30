package org.catclub.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatResponse {
    private Long id;
    private String name;
    private String breed;
    private String color;
    private String gender;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private String description;
    private OwnerResponse owner;
}
