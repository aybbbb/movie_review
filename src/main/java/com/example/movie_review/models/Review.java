package com.example.movie_review.models;

import lombok.Data;

@Data
public class Review {
    int id;
    int memId;
    String movieId;
    String textReview;
    String photoReview;
    int rating;
    int isDel;
    String regDate;
    String editDate;

    String writer;
    float ratingAvg;
}
