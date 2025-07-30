package org.catclub.cat.client;

import org.catclub.shared.dto.OwnerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "owner-service",
        url = "http://owner-service:8080" // Прямое указание URL
)
public interface OwnerServiceClient {
    @GetMapping("/api/owners/{id}")
    OwnerResponse getOwner(@PathVariable Long id);
}