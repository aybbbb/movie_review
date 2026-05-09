package com.example.movie_review.services.impl;

import org.springframework.stereotype.Service;

import com.example.movie_review.exceptions.ServiceNoResultException;
import com.example.movie_review.mappers.WishlistMapper;
import com.example.movie_review.services.WishlistService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistServiceImpl implements WishlistService {
    
    private final WishlistMapper wishlistMapper;

    @Override
    public boolean getMyWishId(int memId, String movieId) throws Exception {

        Integer myWish = null;
        myWish = wishlistMapper.getWishlistId(memId, movieId);

        return  myWish != null && myWish == 0;
    }

    @Override
    public boolean toggleWishlist(int memId, String movieId) throws Exception {
       
        wishlistMapper.toggleWishlist(memId, movieId);

        Integer myWish = wishlistMapper.getWishlistId(memId, movieId);

        return myWish != null && myWish == 0;
    }
    
}
