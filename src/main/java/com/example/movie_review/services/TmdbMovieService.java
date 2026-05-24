package com.example.movie_review.services;

import java.util.List;

import com.example.movie_review.models.Movie;

public interface TmdbMovieService {

    public List<Movie> searchMovie(String keyword, int page) throws Exception;
    public List<Movie> getPopularMovies(int page) throws Exception ;
    public Movie getDetailItem(String movieId) throws Exception;
}
