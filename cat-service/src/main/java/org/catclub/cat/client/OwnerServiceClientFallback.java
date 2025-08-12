package org.catclub.cat.client;

import lombok.extern.slf4j.Slf4j;
import org.catclub.shared.dto.OwnerResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OwnerServiceClientFallback implements OwnerServiceClient {

    @Override
    public OwnerResponse getOwner(Long id) {
        log.warn("Fallback triggered for owner-service, id: {}", id);
        return OwnerResponse.builder()
                .id(id != null ? id : -1L)
                .name("Unknown Owner (Fallback)")
                .email("unknown@example.com")
                .phone("N/A")
                .build();
    }
}