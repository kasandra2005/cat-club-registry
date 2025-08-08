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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owners")
@Tag(name = "Owner Management", description = "Endpoints for managing cat owners")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @Operation(summary = "Create a new owner")
    @ApiResponse(responseCode = "201", description = "Owner created")

    @PostMapping
    public ResponseEntity<OwnerResponse> createOwner(@Valid @RequestBody OwnerRequest request) {
        return ResponseEntity.ok(ownerService.createOwner(request));
    }

    @Operation(summary = "Get owner by id")
    @ApiResponse(responseCode = "200", description = "Owner information")
    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponse> getOwner(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getOwner(id));
    }
}
