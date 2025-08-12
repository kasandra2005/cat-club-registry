package org.catclub.cat.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.catclub.cat.client.OwnerServiceClient;
import org.catclub.cat.dto.CatRequest;
import org.catclub.cat.dto.CatResponse;
import org.catclub.cat.model.Cat;
import org.catclub.cat.repository.CatRepository;
import org.catclub.shared.dto.OwnerResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatService {
    private final CatRepository repository;
    private final OwnerServiceClient ownerServiceClient;

    public CatResponse createCat(CatRequest request) {
        Cat cat = new Cat();
        cat.setName(request.getName());
        cat.setBreed(request.getBreed());
        cat.setColor(request.getColor());
        cat.setOwnerId(request.getOwnerId());
        cat.setBirthDate(request.getBirthDate());
        cat.setGender(request.getGender());
        cat.setDescription(request.getDescription());

        if (request.getMotherId() != null && request.getMotherId() > 0) {
            cat.setMotherId(request.getMotherId());
        }
        if (request.getFatherId() != null && request.getFatherId() > 0) {
            cat.setFatherId(request.getFatherId());
        }

        Cat saved = repository.save(cat);
        return mapToDto(saved);
    }

    @CircuitBreaker(name = "ownerServiceClient", fallbackMethod = "getCatFallback")
    @Cacheable(cacheNames = "cats", cacheManager = "catCacheManager")
    public CatResponse getCat(Long id) {
        Cat cat = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cat not found"));

        try {
            OwnerResponse owner = ownerServiceClient.getOwner(cat.getOwnerId());
            return buildCatResponse(cat, owner);
        } catch (Exception e) {
            log.error("Error calling owner service", e);
            throw new RuntimeException("Service unavailable");
        }
    }

    private CatResponse getCatFallback(Long id, Exception ex) {
        log.warn("Fallback triggered for cat id: {}, error: {}", id, ex.getMessage());
        return CatResponse.builder()
                .id(id)
                .name("Fallback Cat")
                .owner(createFallbackOwnerResponse(null))
                .build();
    }

    public CatResponse getCatParents(Long id) {
        Cat cat = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cat not found"));

        CatResponse response = mapToDto(cat);

        if (cat.getMotherId() != null) {
            try {
                Cat mother = repository.findById(cat.getMotherId())
                        .orElseThrow(() -> new RuntimeException("Mother cat not found"));
                response.setMother(mapToParentDto(mother));
            } catch (Exception e) {
                log.warn("Error fetching mother cat, id: {}", cat.getMotherId(), e);
                response.setMother(createFallbackParentResponse(cat.getMotherId()));
            }
        }
        if (cat.getFatherId() != null) {
            try {
                Cat father = repository.findById(cat.getFatherId())
                        .orElseThrow(() -> new RuntimeException("Father cat not found"));
                response.setFather(mapToParentDto(father));
            } catch (Exception e) {
                log.warn("Error fetching father cat, id: {}", cat.getFatherId(), e);
                response.setFather(createFallbackParentResponse(cat.getFatherId()));
            }
        }

        return response;
    }

    private CatResponse mapToDto(Cat cat) {
        try {
            OwnerResponse owner = ownerServiceClient.getOwner(cat.getOwnerId());
            return buildCatResponse(cat, owner);
        } catch (Exception e) {
            log.warn("Error fetching owner data for cat {}, using fallback", cat.getId(), e);
            return buildCatResponse(cat, createFallbackOwnerResponse(cat.getOwnerId()));
        }
    }

    private CatResponse buildCatResponse(Cat cat, OwnerResponse owner) {
        return CatResponse.builder()
                .id(cat.getId())
                .name(cat.getName())
                .breed(cat.getBreed())
                .color(cat.getColor())
                .gender(cat.getGender())
                .birthDate(cat.getBirthDate())
                .registrationDate(cat.getRegistrationDate())
                .description(cat.getDescription())
                .owner(owner)
                .build();
    }

    private CatResponse.CatParentResponse mapToParentDto(Cat cat) {
        return CatResponse.CatParentResponse.builder()
                .id(cat.getId())
                .name(cat.getName())
                .breed(cat.getBreed())
                .build();
    }

    private OwnerResponse createFallbackOwnerResponse(Long ownerId) {
        return OwnerResponse.builder()
                .id(ownerId != null ? ownerId : -1L)
                .name("Unknown Owner")
                .email("unknown@example.com")
                .phone("N/A")
                .build();
    }

    private CatResponse.CatParentResponse createFallbackParentResponse(Long parentId) {
        return CatResponse.CatParentResponse.builder()
                .id(parentId != null ? parentId : -1L)
                .name("Unknown Parent")
                .breed("Unknown Breed")
                .build();
    }
}