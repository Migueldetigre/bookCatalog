package com.alurachallenges.literalura.bookfinder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UmwandelnDaten implements UmwandelnDateneingabe{
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T abrufenDaten(String json, Class<T> Klasse){
        try{
            return objectMapper.readValue(json, Klasse);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
