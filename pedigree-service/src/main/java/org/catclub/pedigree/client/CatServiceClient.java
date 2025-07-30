package org.catclub.pedigree.client;

import org.catclub.shared.dto.CatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cat-service", url = "http://cat-service:8081")
public interface CatServiceClient {
    @GetMapping("/api/cats/{id}")
    CatResponse getCat(@PathVariable Long id);
}