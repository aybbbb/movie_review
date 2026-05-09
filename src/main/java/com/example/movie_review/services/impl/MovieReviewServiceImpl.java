package com.example.movie_review.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.movie_review.exceptions.ServiceNoResultException;
import com.example.movie_review.mappers.MovieReviewMapper;
import com.example.movie_review.models.Review;
import com.example.movie_review.services.MovieReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieReviewServiceImpl implements MovieReviewService{
    
    private final MovieReviewMapper movieReviewMapper;

    @Override
    public List<Review> movieReviews(String movieId, String movieGbn) throws Exception {

        List<Review> output;

        output = movieReviewMapper.moveiReiviewList(movieId, movieGbn);
        
        return output;
    }



    @Override
    public void insertMovieReview(Review review) throws Exception {
       
        if(movieReviewMapper.movieReviewInsrt(review) == 0){
           throw new ServiceNoResultException("등록에 실패했습니다.");
        }
    }

    @Override
    public void deleteMovieReview(int reviewId, int memberId) throws Exception {

        Review review = movieReviewMapper.moveiReiview(reviewId);

        if (review == null) {
            throw new RuntimeException("존재하지 않는 리뷰입니다.");
        }

        // 본인 리뷰인지 확인
        if (review.getMemId() != memberId) {
            throw new RuntimeException("본인 리뷰만 삭제할 수 있습니다.");
        }

        if(movieReviewMapper.deleteReview(reviewId) == 0){
           throw new ServiceNoResultException("삭제에 실패했습니다.");
        }
    }

    @Override
    public void updateMovieReview(Review review, int memberId) throws Exception {

        if (review.getMemId() != memberId) {
            throw new RuntimeException("본인 리뷰만 삭제할 수 있습니다.");
        }
        if(movieReviewMapper.updateReview(review) == 0){
           throw new ServiceNoResultException("등록에 실패했습니다.");
        }
    }



    @Override
    public Review movieReviewOne(int reviewId) throws Exception {
       Review review = movieReviewMapper.moveiReiview(reviewId);

       return review;
    }



    @Override
    public float movieAvgRating(String movieId, String movieGbn) throws Exception {

        return movieReviewMapper.selectRatingAvg(movieId, movieGbn);
    }



    @Override
    public Integer myReviewId(String movieId, int memberId, String movieGbn) throws Exception {

        Integer myReviewId = null;
        myReviewId = movieReviewMapper.myReviewId(movieId, memberId,movieGbn);

       return myReviewId;
    }


}
