package com.example.movie_review.models;

import java.util.List;

import lombok.Data;

@Data
public class MovieSearch {

    private List<MovieItem> Search;
    private String totalResults;
    private String Response;
    private String Error;
}
