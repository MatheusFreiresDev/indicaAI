package com.indicaAI.service;

import com.indicaAI.dtos.TmbdResponse;
import com.indicaAI.dtos.TmdbMovieDto;
import com.indicaAI.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class TmbdService {

    @Value("${tmdb.api.key}")
    String apiKey;
    @Value("${tmdb.api.url}")
    String url;
    private final WebClient webClient = WebClient.create();

    public List<TmdbMovieDto> searchFilm(String tittle){
        String url = makeUrl(tittle);
        TmbdResponse response =  webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(TmbdResponse.class)
                .block();
        return response.results();
    }

    private String makeUrl(String title) {
        return url + "/search/movie?api_key=" + apiKey + "&query=" + title + "&language=pt-BR";
    }
}
