package com.example.movie_review.services;

public interface WishlistService {

    public boolean getMyWishId(int memId, String movieId, String movieGbn) throws Exception;
    public boolean toggleWishlist(int memId, String movieId, String movieGbn) throws Exception;
}
