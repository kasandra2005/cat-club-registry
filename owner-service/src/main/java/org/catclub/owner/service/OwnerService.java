package org.catclub.owner.service;

import org.catclub.owner.dto.OwnerRequest;
import org.catclub.owner.model.Owner;
import org.catclub.owner.repository.OwnerRepository;
import org.catclub.shared.dto.OwnerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository repository;

    @Cacheable("owners")
    public OwnerResponse getOwner(Long id) {
        return repository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
    }

    @CacheEvict(value = "owners", allEntries = true)
    public OwnerResponse createOwner(OwnerRequest request) {
        Owner owner = new Owner();
        owner.setName(request.name());
        owner.setEmail(request.email());
        owner.setPhone(request.phone());
        owner.setRegistrationDate(LocalDate.now());

        Owner saved = repository.save(owner);
        return mapToDto(saved);
    }

    private OwnerResponse mapToDto(Owner owner) {
        return new OwnerResponse(
                owner.getId(),
                owner.getName(),
                owner.getEmail(),
                owner.getPhone(),
                owner.getRegistrationDate()
        );
    }
}