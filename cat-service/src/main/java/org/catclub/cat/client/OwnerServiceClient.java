package org.catclub.cat.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.catclub.shared.dto.OwnerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "owner-service",
        url = "http://owner-service:8080",
        fallback = OwnerServiceClientFallback.class
)
public interface OwnerServiceClient {
    @GetMapping("/api/owners/{id}")
    @CircuitBreaker(name = "ownerServiceClient")
    OwnerResponse getOwner(@PathVariable Long id);
}