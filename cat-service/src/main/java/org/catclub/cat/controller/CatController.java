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

@RestController
@RequestMapping("/api/cats")
@Tag(name = "Cat Management", description = "Endpoints for managing cats")
@RequiredArgsConstructor
public class CatController {
    private final CatService catService;

    @Operation(summary = "Create a new cat")
    @ApiResponse(responseCode = "201", description = "Cat created")
    @PostMapping
    public ResponseEntity<CatResponse> createCat(@RequestBody CatRequest request) {
        return ResponseEntity.ok(catService.createCat(request));
    }

    @Operation(summary = "Get cat by id")
    @ApiResponse(responseCode = "200", description = "Cat information")
    @GetMapping("/{id}")
    public ResponseEntity<CatResponse> getCat(@PathVariable Long id) {
        return ResponseEntity.ok(catService.getCat(id));
    }

    @Operation(summary = "Get cat's parents by id")
    @ApiResponse(responseCode = "200", description = "Cat's parents information")
    @GetMapping("/{id}/parents")
    public ResponseEntity<CatResponse> getCatParents(@PathVariable Long id) {
        return ResponseEntity.ok(catService.getCatParents(id));
    }
}
