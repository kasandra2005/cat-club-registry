package org.catclub.cat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.catclub.cat.dto.CatRequest;
import org.catclub.cat.dto.CatResponse;
import org.catclub.cat.service.CatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.annotation.Counted;

@RestController
@RequestMapping("/api/cats")
@Tag(name = "Cat Management", description = "Endpoints for managing cats")
@RequiredArgsConstructor
@Timed(value = "cat.controller", description = "Metrics for all CatController methods")
public class CatController {
    private final CatService catService;

    @Operation(summary = "Create a new cat")
    @ApiResponse(responseCode = "201", description = "Cat created")
    @PostMapping
    @Timed(value = "cat.create", longTask = true, description = "Time taken to create a cat")
    @Counted(value = "cat.create.count", description = "Count of cat creation attempts")
    public ResponseEntity<CatResponse> createCat(@RequestBody CatRequest request) {
        return ResponseEntity.ok(catService.createCat(request));
    }

    @Operation(summary = "Get cat by id")
    @ApiResponse(responseCode = "200", description = "Cat information")
    @GetMapping("/{id}")
    @Timed(value = "cat.get.by_id", description = "Time taken to get cat by ID")
    @Counted(value = "cat.get.by_id.count", description = "Count of get cat by ID requests")
    public ResponseEntity<CatResponse> getCat(@PathVariable Long id) {
        return ResponseEntity.ok(catService.getCat(id));
    }

    @Operation(summary = "Get cat's parents by id")
    @ApiResponse(responseCode = "200", description = "Cat's parents information")
    @GetMapping("/{id}/parents")
    @Timed(value = "cat.get.parents", description = "Time taken to get cat's parents")
    @Counted(value = "cat.get.parents.count", description = "Count of get cat parents requests")
    @Timed(value = "cat.complex.operation", description = "Time for complex operations")
    public ResponseEntity<CatResponse> getCatParents(@PathVariable Long id) {
        return ResponseEntity.ok(catService.getCatParents(id));
    }
}