package org.catclub.pedigree.controller;

import org.catclub.pedigree.dto.PedigreeRequest;
import org.catclub.pedigree.dto.PedigreeResponse;
import org.catclub.pedigree.service.PedigreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedigrees")
@RequiredArgsConstructor
public class PedigreeController {
    private final PedigreeService pedigreeService;

    @PostMapping
    public ResponseEntity<PedigreeResponse> createPedigree(@RequestBody PedigreeRequest request) {
        return ResponseEntity.ok(pedigreeService.createPedigree(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedigreeResponse> getPedigree(@PathVariable Long id) {
        return ResponseEntity.ok(pedigreeService.getPedigree(id));
    }

    @GetMapping("/cat/{catId}")
    public ResponseEntity<PedigreeResponse> getPedigreeByCatId(@PathVariable Long catId) {
        return ResponseEntity.ok(pedigreeService.getPedigreeByCatId(catId));
    }
}