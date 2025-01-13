package com.alurachallenges.literalura.bookfinder.bookfinderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alurachallenges.literalura.bookfinder.model.Buch;

import java.util.List;

public interface BuchRepository extends JpaRepository<Buch, Long> {
    List<Buch> findBySpracheContains(String sprache);
}