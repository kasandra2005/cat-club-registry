package org.catclub.owner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.catclub.owner.dto.OwnerRequest;
import org.catclub.owner.service.OwnerService;
import org.catclub.shared.dto.OwnerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.annotation.Counted;

@RestController
@RequestMapping("/api/owners")
@Tag(name = "Owner Management", description = "Endpoints for managing cat owners")
@RequiredArgsConstructor
@Timed(value = "owner.controller", description = "Metrics for all OwnerController methods")
public class OwnerController {
    private final OwnerService ownerService;

    @Operation(summary = "Create a new owner")
    @ApiResponse(responseCode = "201", description = "Owner created")
    @PostMapping
    @Timed(value = "owner.create",
            longTask = true,
            extraTags = {"operation", "create"},
            description = "Time taken to create owner")
    @Counted(value = "owner.create.count",
            description = "Count of owner creation attempts",
            extraTags = {"category", "write"})
    public ResponseEntity<OwnerResponse> createOwner(@Valid @RequestBody OwnerRequest request) {
        return ResponseEntity.ok(ownerService.createOwner(request));
    }

    @Operation(summary = "Get owner by id")
    @ApiResponse(responseCode = "200", description = "Owner information")
    @GetMapping("/{id}")
    @Timed(value = "owner.get.by_id",
            extraTags = {"operation", "read"},
            description = "Time taken to get owner by ID")
    @Counted(value = "owner.get.by_id.count",
            description = "Count of get owner by ID requests",
            extraTags = {"category", "read"})
    public ResponseEntity<OwnerResponse> getOwner(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getOwner(id));
    }
}