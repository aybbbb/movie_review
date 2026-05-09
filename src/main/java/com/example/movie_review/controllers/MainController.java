package com.example.movie_review.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.movie_review.models.MovieItem;
import com.example.movie_review.models.MovieSearch;
import com.example.movie_review.models.Review;
import com.example.movie_review.services.MovieReviewService;
import com.example.movie_review.services.OmdbMovieService;


@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final OmdbMovieService omdbMovieService;
    private final MovieReviewService movieReviewService;

    @GetMapping("/")
    public String mainPage(
        Model model,
        @RequestParam(value = "keyword", required = false) String keyword
    ) throws Exception{

        MovieSearch movieList;

        if(keyword == null || keyword.trim().isEmpty() ){
            movieList = omdbMovieService.getPopularMovies();
        }else{
            movieList = omdbMovieService.searchMovie(keyword);
        }

        model.addAttribute("movieList", movieList.getSearch());
        model.addAttribute("keyword", keyword);

        return "main";
    }

    @GetMapping("/movie/detail/{imdbId}")
    public String detailMovie( @PathVariable("imdbId") String imdbId, Model model)throws Exception {

        MovieItem outMovie = omdbMovieService.getDetailItem(imdbId);
        List<Review> reviewList = movieReviewService.movieReviews(imdbId);
        float ratingAvg = movieReviewService.movieAvgRating(imdbId);

        model.addAttribute("movie", outMovie);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("ratingAvg", ratingAvg);

        return "movie/detail";

    }
    
    
    
}
