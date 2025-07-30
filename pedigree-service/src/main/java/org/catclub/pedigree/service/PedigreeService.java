package org.catclub.pedigree.service;

import org.catclub.pedigree.client.CatServiceClient;
import org.catclub.pedigree.dto.PedigreeRequest;
import org.catclub.pedigree.dto.PedigreeResponse;
import org.catclub.pedigree.model.Pedigree;
import org.catclub.pedigree.repository.PedigreeRepository;
import org.catclub.shared.dto.CatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PedigreeService {
    private final PedigreeRepository repository;
    private final CatServiceClient catServiceClient;

    @CacheEvict(value = "pedigrees", allEntries = true)
    public PedigreeResponse createPedigree(PedigreeRequest request) {
        Pedigree pedigree = new Pedigree();
        pedigree.setCatId(request.getCatId());
        pedigree.setRegistrationNumber(request.getRegistrationNumber());
        pedigree.setIssueDate(LocalDate.now());
        pedigree.setMotherPedigreeId(request.getMotherPedigreeId());
        pedigree.setFatherPedigreeId(request.getFatherPedigreeId());

        Pedigree saved = repository.save(pedigree);
        return mapToDto(saved);
    }

    @Cacheable(value = "pedigrees", key = "#id")
    public PedigreeResponse getPedigree(Long id) {
        Pedigree pedigree = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedigree not found"));
        return mapToDto(pedigree);
    }

    @Cacheable(value = "pedigrees", key = "'cat_' + #catId")
    public PedigreeResponse getPedigreeByCatId(Long catId) {
        Pedigree pedigree = repository.findByCatId(catId)
                .orElseThrow(() -> new RuntimeException("Pedigree not found for cat"));
        return mapToDto(pedigree);
    }

    private PedigreeResponse mapToDto(Pedigree pedigree) {
        CatResponse cat = catServiceClient.getCat(pedigree.getCatId());

        PedigreeResponse.PedigreeResponseBuilder builder = PedigreeResponse.builder()
                .id(pedigree.getId())
                .registrationNumber(pedigree.getRegistrationNumber())
                .issueDate(pedigree.getIssueDate())
                .cat(cat);

        if (pedigree.getMotherPedigreeId() != null) {
            builder.mother(getPedigree(pedigree.getMotherPedigreeId()));
        }
        if (pedigree.getFatherPedigreeId() != null) {
            builder.father(getPedigree(pedigree.getFatherPedigreeId()));
        }

        return builder.build();
    }
}