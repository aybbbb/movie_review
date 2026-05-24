package com.example.movie_review.models;

import lombok.Data;

@Data
public class Review {
    private int id;
    private int memId;
    private String movieId;
    private String textReview;
    private String photoReview;
    private int rating;
    private int isDel;
    private String regDate;
    private String editDate;

    private String writer;
    private float ratingAvg;
    private String movieGbn;

    private String movieTitle;
    private String moviePoster;

}
