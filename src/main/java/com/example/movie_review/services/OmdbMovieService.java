package com.example.movie_review.services;

import java.util.List;

import com.example.movie_review.models.Movie;
import com.example.movie_review.models.OmdbMovieSearch;

public interface OmdbMovieService {

    public List<Movie> searchMovie(String keyword) throws Exception;
    public List<Movie> getPopularMovies() throws Exception ;
    public Movie getDetailItem(String movieId) throws Exception;
}
