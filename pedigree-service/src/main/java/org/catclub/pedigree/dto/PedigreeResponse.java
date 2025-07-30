package org.catclub.pedigree.dto;

import org.catclub.shared.dto.CatResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedigreeResponse {
    private Long id;
    private String registrationNumber;
    private LocalDate issueDate;
    private CatResponse cat;
    private PedigreeResponse mother;
    private PedigreeResponse father;
}