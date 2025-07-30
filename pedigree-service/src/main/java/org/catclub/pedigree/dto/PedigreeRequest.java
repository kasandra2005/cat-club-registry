package org.catclub.pedigree.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedigreeRequest {
    @NotNull(message = "Cat ID is required")
    @Positive(message = "Cat ID must be positive")
    private Long catId;

    @NotNull(message = "Registration number is required")
    private String registrationNumber;

    private Long motherPedigreeId;
    private Long fatherPedigreeId;
}