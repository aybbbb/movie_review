package com.example.movie_review.services.impl;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.movie_review.helpers.OmdbHelper;
import com.example.movie_review.models.MovieItem;
import com.example.movie_review.models.MovieSearch;
import com.example.movie_review.services.OmdbMovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieServiceImpl implements OmdbMovieService {
    
    private final OmdbHelper omdbHelper;

    @Override
    public MovieSearch searchMovie(String keyword) throws Exception{
        
        log.info("영화 검색 keyword={}", keyword);
        return omdbHelper.searchMovie(keyword);
    }

    @Override
    public MovieSearch getPopularMovies()throws Exception {
        String[] popularKeywords = {
                "marvel",
                "batman",
                "avengers",
                "harry potter",
                "star wars"
        };

        Random random = new Random();
        int index = random.nextInt(popularKeywords.length);

        String keyword = popularKeywords[index];

        log.info("기본 인기영화 keyword={}", keyword);

        return omdbHelper.searchMovie(keyword);
    }

    @Override
    public MovieItem getDetailItem(String movieId) throws Exception {
       
        log.info("영화 상세조회 imdbId={}", movieId);
        
        return omdbHelper.getMovieDetail(movieId);
    }
    
}
