package com.alurachallenges.literalura.bookfinder.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Daten (
        @JsonAlias("results") List<BücherDaten> ergebnisse
){
}
