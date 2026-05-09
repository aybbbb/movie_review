package com.example.movie_review.services;

import java.util.List;

import com.example.movie_review.models.Review;

public interface MovieReviewService {
    
    public List<Review> movieReviews(String movieId) throws Exception;
    public void insertMovieReview(Review review) throws Exception;
    public void deleteMovieReview(int reviewId, int memberId) throws Exception;
    public void updateMovieReview(Review review, int memberId) throws Exception;
    public Review movieReviewOne(int reviewId) throws Exception;
    public float movieAvgRating(String movieId)  throws Exception;
    public Integer myReviewId(String movieId, int memberId) throws Exception;
}
