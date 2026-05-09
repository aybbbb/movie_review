package com.example.movie_review.models;

import java.util.List;

import lombok.Data;

@Data
public class OmdbMovieSearch {

    private List<OmdbMovieDetail> Search;
    private String totalResults;
    private String Response;
    private String Error;
}
