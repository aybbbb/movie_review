package com.example.movie_review.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TmdbMovieDetail extends TmdbMovieResult {

    /** 상영 시간(분) */
    private Integer runtime;

    /** IMDb ID */
    @JsonProperty("imdb_id")
    private String imdbId;

    private TmdbMovieCredits credits;
}