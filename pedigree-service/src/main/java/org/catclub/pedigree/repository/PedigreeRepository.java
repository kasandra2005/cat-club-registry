package org.catclub.pedigree.repository;

import org.catclub.pedigree.model.Pedigree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedigreeRepository extends JpaRepository<Pedigree, Long> {
    Optional<Pedigree> findByCatId(Long catId);
}