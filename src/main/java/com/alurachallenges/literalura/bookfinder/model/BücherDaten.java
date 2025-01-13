package com.alurachallenges.literalura.bookfinder.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BücherDaten(
    @JsonAlias("title") String titel,
    @JsonAlias("authors") List<AutorDaten> autor,
    @JsonAlias("languages") List<String> sprache,
    @JsonAlias("download_count") Double anzahlDerDownloads
) {
    }