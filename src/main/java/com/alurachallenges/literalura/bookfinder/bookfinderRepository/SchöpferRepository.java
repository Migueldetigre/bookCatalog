package com.alurachallenges.literalura.bookfinder.bookfinderRepository;

import com.alurachallenges.literalura.bookfinder.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface SchöpferRepository extends JpaRepository<Autor, Long>{
    Optional<Autor>
    findByNameContains(String nameAutor);

    List<Autor>
    findByBirthDateLessThanEqualAndDeathDateGreaterThanEqual(
            Integer BirthDate,
            Integer DeathDate);
}
