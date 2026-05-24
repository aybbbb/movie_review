package com.example.movie_review.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.movie_review.models.Member;
import com.example.movie_review.models.Movie;
import com.example.movie_review.models.Review;
import com.example.movie_review.services.MovieReviewService;
import com.example.movie_review.services.MyMovieService;
import com.example.movie_review.services.OmdbMovieService;
import com.example.movie_review.services.TmdbMovieService;

import jakarta.servlet.http.HttpSession;


@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final OmdbMovieService omdbMovieService;
    private final TmdbMovieService tmdbMovieService;
    private final MovieReviewService movieReviewService;
    private final MyMovieService myMovieService;

   @GetMapping({"/", "/{movie_gbn}"})
    public String mainPage(
            @PathVariable(value = "movie_gbn", required = false) String movie_gbn,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model
    ) throws Exception {

        // 기본값: omdb
        if (movie_gbn == null || movie_gbn.isBlank()) {
            movie_gbn = "tmdb";
        }

        List<Movie> movieList;

        // TMDb
        if ("tmdb".equalsIgnoreCase(movie_gbn)) {
            
            if (keyword == null || keyword.trim().isEmpty()) {
                movieList = tmdbMovieService.getPopularMovies(page);
            } else {
                movieList = tmdbMovieService.searchMovie(keyword, page);
            }

        // OMDb
        } else {
            movie_gbn = "omdb";

            if (keyword == null || keyword.trim().isEmpty()) {
                movieList = omdbMovieService.getPopularMovies(page);
            } else {
                movieList = omdbMovieService.searchMovie(keyword, page);
            }
        }

        // 공통 데이터
        model.addAttribute("movieList", movieList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("movieGbn", movie_gbn);
        model.addAttribute("page", page);
        model.addAttribute("currentPage", movie_gbn);

        // 기존 main.html 그대로 사용
        return "main";
    }

    @GetMapping("/movie/{movieGbn}/detail/{id}")
    public String detailMovie( @PathVariable("movieGbn") String movie_gbn, @PathVariable("id") String id, Model model)throws Exception {
        // 기본값: omdb
        if (movie_gbn == null || movie_gbn.isBlank()) {
            movie_gbn = "tmdb";
        }

        Movie outMovie;

        // TMDb
        if ("tmdb".equalsIgnoreCase(movie_gbn)) {
            outMovie = tmdbMovieService.getDetailItem(id);
        // OMDb
        } else {
            outMovie = omdbMovieService.getDetailItem(id);
        }
        
        List<Review> reviewList = movieReviewService.movieReviews(id,movie_gbn);
        float ratingAvg = movieReviewService.movieAvgRating(id,movie_gbn);

        model.addAttribute("movie", outMovie);
        model.addAttribute("movieGbn", movie_gbn);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("ratingAvg", ratingAvg);
        model.addAttribute("currentPage", movie_gbn);

        return "movie/detail";

    }

    @GetMapping("/my/review")
    public String reviewPage(
        HttpSession session,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "page", defaultValue = "1") int page,
        Model model
    ) throws Exception {

       Member memberInfo =(Member) session.getAttribute("memberInfo");

        if (memberInfo == null) {
            return "redirect:/account/login";
        }

        List<Review> reviewList = myMovieService.myMoveiReivew(memberInfo.getUserId(), keyword);

        // 공통 데이터
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("currentPage", "myReview");

        // 기존 main.html 그대로 사용
        return "my/review";
    }
    
    
    
}
