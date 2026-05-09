package com.example.movie_review.services;

import com.example.movie_review.models.MovieItem;
import com.example.movie_review.models.MovieSearch;

public interface OmdbMovieService {

    public MovieSearch searchMovie(String keyword) throws Exception;
    public MovieSearch getPopularMovies() throws Exception ;
    public MovieItem getDetailItem(String movieId) throws Exception;
}
