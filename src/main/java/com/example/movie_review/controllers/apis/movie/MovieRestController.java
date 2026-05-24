package com.example.movie_review.controllers.apis.movie;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.movie_review.helpers.FileHelper;
import com.example.movie_review.helpers.RegexHelper;
import com.example.movie_review.models.Member;
import com.example.movie_review.models.Review;
import com.example.movie_review.models.UploadItem;
import com.example.movie_review.services.MovieReviewService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@Slf4j
@RequiredArgsConstructor
public class MovieRestController {
    
    private final MovieReviewService movieReviewService;

    private final RegexHelper regexHelper;
    private final FileHelper fileHelper;

    @PostMapping("/api/movie/insert")
    public  Map<String, Object> enrollReview(
        @RequestParam("mem_id") int memId,
        @RequestParam("movie_id") String movieId, 
        @RequestParam("rating") int rating, 
        @RequestParam("text_review") String textReview, 
        @RequestParam("movie_gbn") String movieGbn,
        @RequestParam("movie_title") String movieTitle,
        @RequestParam("movie_poster") String moviePoster,
        @RequestParam(value = "photo_review", required = false) MultipartFile photo
        ) throws Exception{
        
        regexHelper.isValue(textReview, "영화 리뷰를 작성해주세요.");

        UploadItem uploadItem = null;

        try {
            uploadItem = fileHelper.saveMultipartFile(photo);
        } catch (Exception e) {
            // TODO: handle exception
        }

        Review input = new Review();
        input.setMemId(memId);
        input.setMovieId(movieId);
        input.setRating(rating);
        input.setTextReview(textReview);
        input.setMovieGbn(movieGbn);
        input.setMovieTitle(movieTitle);
        input.setMoviePoster(moviePoster);

        if(uploadItem != null){
            input.setPhotoReview(uploadItem.getFilePath());
        }
        
        movieReviewService.insertMovieReview(input);

        List<Review> reviewList = movieReviewService.movieReviews(movieId, movieGbn);
        
        Map<String, Object> result = new HashMap<>();

        result.put("reviewList", reviewList);
        
        return result;
    }
    
    @PutMapping("/api/movie/del/{id}")
    public Map<String, Object> delReview(@PathVariable(value = "id", required = true) int id, HttpSession session) throws Exception {
        

        Member memberInfo = (Member) session.getAttribute("memberInfo");

        if (memberInfo == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        movieReviewService.deleteMovieReview(id, memberInfo.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;

    }

    @PutMapping("/api/movie/update")
    public  Map<String, Object> updateReview(
        @RequestParam("id") int id,
        @RequestParam("mem_id") int memId,
        @RequestParam("movie_id") String movieId, 
        @RequestParam("rating_upt") int rating, 
        @RequestParam("text_review_upt") String textReview,  
        @RequestParam("movie_gbn") String movieGbn,
        @RequestParam(value = "photo_review_upt", required = false) MultipartFile photo,
        HttpSession session
        ) throws Exception{
        
        regexHelper.isValue(textReview, "영화 리뷰를 작성해주세요.");
        Member memberInfo = (Member) session.getAttribute("memberInfo");

        UploadItem uploadItem = null;

        if (photo != null && !photo.isEmpty()) {
            try {
                uploadItem = fileHelper.saveMultipartFile(photo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

      

        Review input = new Review();
        input.setId(id);
        input.setMemId(memId);
        input.setMovieId(movieId);
        input.setRating(rating);
        input.setTextReview(textReview);
        input.setMovieGbn(movieGbn);

        // 새 사진 업로드한 경우만 변경
        if (uploadItem != null) {
            input.setPhotoReview(uploadItem.getFilePath());
        } else {
            // 새 사진 없으면 기존 사진 유지
            Review origin = movieReviewService.movieReviewOne(id);
            input.setPhotoReview(origin.getPhotoReview());
        }
        
        movieReviewService.updateMovieReview(input,memberInfo.getId());

        List<Review> reviewList = movieReviewService.movieReviews(movieId,movieGbn);
        
        Map<String, Object> result = new HashMap<>();

        result.put("reviewList", reviewList);
        
        return result;
    }

    @GetMapping("/api/movie/my")
    public Integer getMyReviewId( @RequestParam("movie_id") String movieId, @RequestParam("movie_gbn") String movieGbn, HttpSession httpSession)throws Exception {

        Member memberInfo = (Member) httpSession.getAttribute("memberInfo");

        if (memberInfo == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        Integer reviewId = movieReviewService.myReviewId(movieId, memberInfo.getId(),movieGbn);

        return  reviewId != null ? reviewId : 0;

    }
    
}
