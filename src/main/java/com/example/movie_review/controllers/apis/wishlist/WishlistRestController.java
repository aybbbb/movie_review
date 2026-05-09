package com.example.movie_review.controllers.apis.wishlist;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie_review.models.Member;
import com.example.movie_review.services.WishlistService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@Slf4j
@RequiredArgsConstructor
public class WishlistRestController {
    
    private final WishlistService wishlistService;

    @GetMapping("/api/wishlist/is_wished")
    public Map<String, Object> isWished( @RequestParam("movie_id") String movieId, @RequestParam("movie_gbn") String movieGbn, HttpSession httpSession)throws Exception {

        Member memberInfo = (Member) httpSession.getAttribute("memberInfo");

        if (memberInfo == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

       boolean isWished = wishlistService.getMyWishId(memberInfo.getId(), movieId, movieGbn);

       Map<String, Object> result = new HashMap<>();
       result.put("isWished", isWished);

       return result;

    }

    @PutMapping("/api/wishlist/toggle")
    public Map<String, Object> toggleWish(@RequestParam("movie_id") String movieId, @RequestParam("movie_gbn") String movieGbn, HttpSession httpSession) throws Exception {
        Member memberInfo = (Member) httpSession.getAttribute("memberInfo");

        if (memberInfo == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

       boolean isWished = wishlistService.toggleWishlist(memberInfo.getId(), movieId,movieGbn);

       Map<String, Object> result = new HashMap<>();
       result.put("toggleWish", isWished);

       return result;
    }



}
