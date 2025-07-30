package org.catclub.cat.repository;

import org.catclub.cat.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {

    // 1. Стандартные методы (уже есть в JpaRepository)
    // save(), findById(), findAll(), deleteById() и т.д.

    // 2. Кастомные запросы через именованные методы
    List<Cat> findByOwnerId(Long ownerId);

    List<Cat> findByBreed(String breed);

    List<Cat> findByColorAndBreed(String color, String breed);

    Optional<Cat> findByNameAndOwnerId(String name, Long ownerId);

    // 3. JPQL запросы
    @Query("SELECT c FROM Cat c WHERE c.motherId = :catId OR c.fatherId = :catId")
    List<Cat> findChildren(@Param("catId") Long catId);

    @Query("SELECT c FROM Cat c WHERE c.ownerId = :ownerId AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Cat> findByOwnerAndNameContainingIgnoreCase(@Param("ownerId") Long ownerId,
                                                     @Param("name") String name);

    // 4. Нативные SQL запросы для сложных случаев
    @Query(value = """
        SELECT * FROM cats 
        WHERE breed = :breed 
        AND birth_date BETWEEN :startDate AND :endDate
        ORDER BY registration_date DESC
        LIMIT 10
        """, nativeQuery = true)
    List<Cat> findTop10ByBreedAndBirthDateBetween(@Param("breed") String breed,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    // 5. Проверки существования
    boolean existsByIdAndOwnerId(Long id, Long ownerId);
}
