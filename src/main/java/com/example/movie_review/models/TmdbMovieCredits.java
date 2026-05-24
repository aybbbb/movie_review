package com.example.movie_review.models;

import java.util.List;

import lombok.Data;

@Data
public class TmdbMovieCredits {
    private List<TmdbMovieCast> cast;
    private List<TmdbMovieCrew> crew;
}
