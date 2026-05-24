package com.example.movie_review.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.movie_review.models.OmdbMovieDetail;
import com.example.movie_review.models.TmdbMovieDetail;
import com.example.movie_review.models.TmdbMovieSearch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TmdbHelper {

    private final RestTemplate restTemplate;

    @Value("${tmdb.api.key}")
    private String tmdbKey;

    @Value("${tmdb.base.url}")
    private String tmdbUrl;

    /**
     * 인기 영화
     */
    public TmdbMovieSearch getPopularMovies(int page) {

        String url = tmdbUrl + "/movie/popular?api_key=" + tmdbKey + "&language=ko-KR&page="+page;

        log.info("TMDb Search URL = {}", url);

        return restTemplate.getForObject(url, TmdbMovieSearch.class);
    }

    /**
     * 영화 검색
     */
    public TmdbMovieSearch searchMovie(String keyword, int page) {

        String url = tmdbUrl + "/search/movie?api_key=" + tmdbKey + "&query=" + keyword + "&language=ko-KR&page="+page;

        log.info("TMDb Search URL = {}", url);

        return restTemplate.getForObject(url, TmdbMovieSearch.class);
    }


    /**
     * 영화 상세 조회
     */
    public TmdbMovieDetail getMovieDetail(String movieId) {
    
        String url = tmdbUrl + "/movie/" +movieId +"?api_key=" + tmdbKey + "&language=ko-KR&append_to_response=credits";

        log.info("TMDb Search URL = {}", url);

        return restTemplate.getForObject(url, TmdbMovieDetail.class);
    
    }


}
