package com.example.movie_review.helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.movie_review.models.MovieItem;
import com.example.movie_review.models.MovieSearch;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OmdbHelper {
    
    private final RestTemplate restTemplate;

    @Value("${omdb.api.key}") 
    private String omdbKey;
    @Value("${omdb.api.url}") 
    private String omdbUrl;

    public MovieSearch searchMovie(String keyword) {
        String url = omdbUrl + "?apikey=" + omdbKey + "&s=" + keyword;

        return restTemplate.getForObject(url, MovieSearch.class);
    }

    public MovieItem getMovieDetail(String imdbId) {

        String url = omdbUrl + "?apikey=" + omdbKey + "&i=" + imdbId;

        return restTemplate.getForObject(url, MovieItem.class);
    }
}
