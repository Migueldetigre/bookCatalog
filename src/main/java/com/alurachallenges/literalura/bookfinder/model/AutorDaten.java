package com.alurachallenges.literalura.bookfinder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAlias;
@JsonIgnoreProperties(ignoreUnknown = true)
public record AutorDaten (
    @JsonAlias("name") String name,
    @JsonAlias("birth_year") String birthDate,
    @JsonAlias("death_year") String deathDate
){
    public static final int deathDate = "death_year";
}
