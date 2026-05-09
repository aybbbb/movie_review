package com.example.movie_review.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TmdbMovieResult {

    /** TMDb 영화 ID */
    private Integer id;

    /** 제목 */
    private String title;

    /** 줄거리 */
    private String overview;

    /** 개봉일 */
    @JsonProperty("release_date")
    private String releaseDate;

    /** 포스터 경로 */
    @JsonProperty("poster_path")
    private String posterPath;

    /** 평균 평점 */
    @JsonProperty("vote_average")
    private Double voteAverage;
}