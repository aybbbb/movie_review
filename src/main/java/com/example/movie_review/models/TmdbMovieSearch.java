package com.example.movie_review.models;

import java.util.List;
import lombok.Data;

@Data
public class TmdbMovieSearch {
    private List<TmdbMovieResult> results;
}