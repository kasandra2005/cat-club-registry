package org.catclub.cat.dto;

import lombok.*;
import org.catclub.shared.dto.OwnerResponse;

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
    private CatParentResponse mother;
    private CatParentResponse father;

    @Data
    @Builder
    public static class CatParentResponse {
        private Long id;
        private String name;
        private String breed;
    }
}