package com.example.movie_review.services;

import java.util.List;

import com.example.movie_review.models.Review;

public interface MyMovieService {
    public List<Review> myMoveiReivew(String memId, String keyword) throws Exception;
}
