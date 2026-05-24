package com.example.movie_review.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.movie_review.mappers.MyMovieMapper;
import com.example.movie_review.models.Movie;
import com.example.movie_review.models.Review;
import com.example.movie_review.services.MyMovieService;
import com.example.movie_review.services.OmdbMovieService;
import com.example.movie_review.services.TmdbMovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyMovieServiceImpl implements MyMovieService {
    
    private final MyMovieMapper myMovieMapper;
    private final TmdbMovieService tmdbMovieService;
    private final OmdbMovieService omdbMovieService;


    @Override
    public List<Review> myMoveiReivew(String memId, String keyword) throws Exception {
        
        List<Review> reviewList = myMovieMapper.myMovieReview(memId, keyword);

        // for (Review review : reviewList) {
        //     Movie movie;
        //     if("tmdb".equals(review.getMovieGbn())){
        //         movie = tmdbMovieService.getDetailItem(review.getMovieId());
        //     }else{
        //         movie = omdbMovieService.getDetailItem(review.getMovieId());
        //     }

        //     review.setMovie(movie);
        // }
        return reviewList;
    }
    
}
