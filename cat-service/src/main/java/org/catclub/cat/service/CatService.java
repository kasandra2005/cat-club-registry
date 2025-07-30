package org.catclub.cat.service;

import lombok.RequiredArgsConstructor;
import org.catclub.cat.client.OwnerServiceClient;
import org.catclub.cat.dto.CatRequest;
import org.catclub.cat.dto.CatResponse;
import org.catclub.cat.model.Cat;
import org.catclub.cat.repository.CatRepository;
import org.catclub.shared.dto.OwnerResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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

    @Cacheable(cacheNames = "cats", cacheManager = "catCacheManager")
    public CatResponse getCat(Long id) {
        Cat cat = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cat not found"));
        return mapToDto(cat);
    }

    public CatResponse getCatParents(Long id) {
        Cat cat = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cat not found"));

        CatResponse response = mapToDto(cat);

        if (cat.getMotherId() != null) {
            Cat mother = repository.findById(cat.getMotherId())
                    .orElseThrow(() -> new RuntimeException("Mother cat not found"));
            response.setMother(mapToParentDto(mother));
        }
        if (cat.getFatherId() != null) {
            Cat father = repository.findById(cat.getFatherId())
                    .orElseThrow(() -> new RuntimeException("Father cat not found"));
            response.setFather(mapToParentDto(father));
        }

        return response;
    }

    private CatResponse mapToDto(Cat cat) {
        OwnerResponse owner = ownerServiceClient.getOwner(cat.getOwnerId());

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
}