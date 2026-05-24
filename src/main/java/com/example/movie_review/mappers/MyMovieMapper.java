package com.example.movie_review.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.example.movie_review.models.Review;

@Mapper
public interface MyMovieMapper {
  
    @Select( "<script> "+
            " SELECT r.id, r.movie_id, r.mem_id, r.text_review, r.photo_review, r.rating, r.is_del, r.reg_date, r.edit_date, "+
            "        m.user_name as writer, m.id, r.movie_gbn, r.movie_title, r.movie_poster " +
            " From reviews as r "+
            " inner join members as m on r.mem_id = m.id "+
            " Where m.user_id = #{memId} and r.is_del  = 0 " +
            "    <if test='keyword != null and keyword != \"\"'>" + 
            "       AND r.movie_title LIKE CONCAT('%', #{keyword}, '%')" +
            "    </if> "+
            "</script>"
        )
    @Results(id= "myMovieReviewList")
    public List<Review> myMovieReview(@Param("memId") String memId, @Param("keyword") String keyword);


    
}
