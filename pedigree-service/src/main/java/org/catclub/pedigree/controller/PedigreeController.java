package org.catclub.pedigree.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.catclub.pedigree.dto.PedigreeRequest;
import org.catclub.pedigree.dto.PedigreeResponse;
import org.catclub.pedigree.service.PedigreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedigrees")
@Tag(name = "Pedigree Management", description = "Endpoints for managing cat's pedigrees")
@RequiredArgsConstructor
public class PedigreeController {
    private final PedigreeService pedigreeService;

    @Operation(summary = "Create a new pedigree")
    @ApiResponse(responseCode = "201", description = "Pedigree created")
    @PostMapping
    public ResponseEntity<PedigreeResponse> createPedigree(@RequestBody PedigreeRequest request) {
        return ResponseEntity.ok(pedigreeService.createPedigree(request));
    }

    @Operation(summary = "Get pedigree by id")
    @ApiResponse(responseCode = "200", description = "Pedigree information")
    @GetMapping("/{id}")
    public ResponseEntity<PedigreeResponse> getPedigree(@PathVariable Long id) {
        return ResponseEntity.ok(pedigreeService.getPedigree(id));
    }

    @Operation(summary = "Get pedigree by cat id")
    @ApiResponse(responseCode = "200", description = "Pedigree information")
    @GetMapping("/cat/{catId}")
    public ResponseEntity<PedigreeResponse> getPedigreeByCatId(@PathVariable Long catId) {
        return ResponseEntity.ok(pedigreeService.getPedigreeByCatId(catId));
    }
}