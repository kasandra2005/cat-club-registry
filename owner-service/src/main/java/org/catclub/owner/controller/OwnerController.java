package org.catclub.owner.controller;

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
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @PostMapping
    public ResponseEntity<OwnerResponse> createOwner(@Valid @RequestBody OwnerRequest request) {
        return ResponseEntity.ok(ownerService.createOwner(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponse> getOwner(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getOwner(id));
    }
}
