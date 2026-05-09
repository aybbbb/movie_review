package com.example.movie_review.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.movie_review.models.OmdbMovieDetail;
import com.example.movie_review.models.OmdbMovieSearch;

import lombok.RequiredArgsConstructor;
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

    /**
     * 영화 검색
     */
    public OmdbMovieSearch searchMovie(String keyword) {

        String url = omdbUrl + "?apikey=" + omdbKey + "&s=" + keyword;

        log.info("OMDb Search URL = {}", url);

        return restTemplate.getForObject(url, OmdbMovieSearch.class);
    }

    /**
     * 영화 상세 조회
     */
    public OmdbMovieDetail getMovieDetail(String imdbId) {

        String url = omdbUrl + "?apikey=" + omdbKey + "&i=" + imdbId + "&plot=full";

        log.info("OMDb Detail URL = {}", url);

        return restTemplate.getForObject(url, OmdbMovieDetail.class);
    }
}