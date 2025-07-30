package org.catclub.cat.controller;

import org.catclub.cat.dto.CatRequest;
import org.catclub.cat.dto.CatResponse;
import org.catclub.cat.service.CatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cats")
@RequiredArgsConstructor
public class CatController {
    private final CatService catService;

    @PostMapping
    public ResponseEntity<CatResponse> createCat(@RequestBody CatRequest request) {
        return ResponseEntity.ok(catService.createCat(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatResponse> getCat(@PathVariable Long id) {
        return ResponseEntity.ok(catService.getCat(id));
    }

    @GetMapping("/{id}/parents")
    public ResponseEntity<CatResponse> getCatParents(@PathVariable Long id) {
        return ResponseEntity.ok(catService.getCatParents(id));
    }
}
