package com.alurachallenges.literalura.bookfinder.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BookFinderConsume {
    public String abrufenDaten(String uri){
        HttpClient kunde = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response = null;
        try{
            response = kunde
                    .send(request, HttpResponse.BodyHandlers.ofString());
        }catch (IOException | InterruptedException e){
            throw new RuntimeException(e);
        }
        String json = response.body();
        return json;
    }
}
