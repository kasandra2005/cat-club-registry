package org.catclub.cat.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatRequest {

    @NotBlank(message = "Name is required")
    @Length(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Breed is required")
    @Length(max = 50, message = "Breed must not exceed 50 characters")
    private String breed;

    @NotBlank(message = "Color is required")
    @Length(max = 30, message = "Color must not exceed 30 characters")
    private String color;

    @NotNull(message = "Owner ID is required")
    @Positive(message = "Owner ID must be positive")
    private Long ownerId;

    @PastOrPresent(message = "Birth date must be in the past or present")
    private LocalDate birthDate;

    @Pattern(regexp = "^(MALE|FEMALE)$", message = "Gender must be MALE or FEMALE")
    private String gender;

    @PositiveOrZero(message = "Mother ID must be positive or zero")
    private Long motherId;

    @PositiveOrZero(message = "Father ID must be positive or zero")
    private Long fatherId;

    @Length(max = 500, message = "Description must not exceed 500 characters")
    private String description;

}