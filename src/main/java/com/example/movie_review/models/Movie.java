package com.example.movie_review.models;

import lombok.Data;

@Data
public class Movie {

    /** 데이터 출처: omdb / tmdb */
    private String movieGbn;

    /** 공통 영화 ID
     *  - OMDb : imdbID (예: tt2488496)
     *  - TMDb : id (예: 550)
     */
    private String movieId;

    /** 기본 정보 */
    private String title;
    private String poster;
    private String plot;

    /** 상세 정보 */
    private String year;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    private String language;
    private String country;
    private String awards;
    private String rated;
    private String type;

    /** 평점 */
    private String imdbRating;
    private String imdbVotes;

    /** 기타 */
    private String dvd;
    private String boxOffice;
    private String production;
}