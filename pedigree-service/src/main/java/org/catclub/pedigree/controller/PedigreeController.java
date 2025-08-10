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
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.annotation.Counted;

@RestController
@RequestMapping("/api/pedigrees")
@Tag(name = "Pedigree Management", description = "Endpoints for managing cat's pedigrees")
@RequiredArgsConstructor
@Timed(value = "pedigree.controller", description = "Metrics for all PedigreeController methods")
public class PedigreeController {
    private final PedigreeService pedigreeService;

    @Operation(summary = "Create a new pedigree")
    @ApiResponse(responseCode = "201", description = "Pedigree created")
    @PostMapping
    @Timed(value = "pedigree.create",
            longTask = true,
            extraTags = {"operation", "create", "complexity", "high"},
            description = "Time taken to create pedigree record")
    @Counted(value = "pedigree.create.count",
            description = "Count of pedigree creation attempts",
            extraTags = {"category", "write"})
    public ResponseEntity<PedigreeResponse> createPedigree(@RequestBody PedigreeRequest request) {
        return ResponseEntity.ok(pedigreeService.createPedigree(request));
    }

    @Operation(summary = "Get pedigree by id")
    @ApiResponse(responseCode = "200", description = "Pedigree information")
    @GetMapping("/{id}")
    @Timed(value = "pedigree.get.by_id",
            extraTags = {"operation", "read", "complexity", "low"},
            description = "Time taken to get pedigree by ID")
    @Counted(value = "pedigree.get.by_id.count",
            description = "Count of get pedigree by ID requests",
            extraTags = {"category", "read"})
    public ResponseEntity<PedigreeResponse> getPedigree(@PathVariable Long id) {
        return ResponseEntity.ok(pedigreeService.getPedigree(id));
    }

    @Operation(summary = "Get pedigree by cat id")
    @ApiResponse(responseCode = "200", description = "Pedigree information")
    @GetMapping("/cat/{catId}")
    @Timed(value = "pedigree.get.by_cat_id",
            extraTags = {"operation", "read", "complexity", "medium"},
            description = "Time taken to get pedigree by cat ID")
    @Timed(value = "pedigree.complex.query",
            description = "Time for complex pedigree queries")
    @Counted(value = "pedigree.get.by_cat_id.count",
            description = "Count of get pedigree by cat ID requests",
            extraTags = {"category", "read"})
    public ResponseEntity<PedigreeResponse> getPedigreeByCatId(@PathVariable Long catId) {
        return ResponseEntity.ok(pedigreeService.getPedigreeByCatId(catId));
    }
}