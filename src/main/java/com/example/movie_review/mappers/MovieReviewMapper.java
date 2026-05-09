package com.example.movie_review.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.movie_review.models.Review;

@Mapper
public interface MovieReviewMapper {

    @Select(" SELECT r.id, r.movie_id, r.mem_id, r.text_review, r.photo_review, r.rating, r.is_del, r.reg_date, r.edit_date, "+
            "        m.user_name as writer, m.id " +
            " From reviews as r "+
            " inner join members as m on r.mem_id = m.id "+
            " Where r.movie_id = #{movieId} and r.movie_gbn = #{movieGbn} and r.is_del  = 0 "
    )
    @Results(id= "movieReviewList")
    public List<Review> moveiReiviewList(@Param("movieId") String movieId, @Param("movieGbn") String movieGbn);
    
    @Select(" SELECT r.id, r.movie_id, r.mem_id, r.text_review, r.photo_review, r.rating, r.is_del, r.reg_date, r.edit_date, "+
            "        m.user_name as writer, m.id " +
            " From reviews as r "+
            " inner join members as m on r.mem_id = m.id "+
            " Where r.id = #{id} and r.is_del  = 0 "
    )
    @Results(id= "movieReview")
    public Review moveiReiview(int id);

    @Select(" SELECT IFNULL(r.id, 0) AS id" +
            " From reviews as r "+
            " Where r.movie_id = #{movieId} and r.mem_id = #{memId} and r.movie_gbn =#{movieGbn} and r.is_del  = 0 "+
            " LIMIT 1 "
    )  
    public Integer myReviewId(@Param("movieId")  String movieId, @Param("memId") int memId , @Param("movieGbn") String movieGbn);



    @Select("SELECT IFNULL(ROUND(AVG(rating),1),0) AS ratingAvg FROM reviews WHERE movie_id =#{movieId} and movie_gbn =#{movieGbn} and is_del = 0")
    public float selectRatingAvg(@Param("movieId") String movieId,  @Param("movieGbn") String movieGbn);

    @Insert("Insert into reviews (mem_id, movie_id, text_review, photo_review, rating, is_del, reg_date, edit_date, movie_gbn ) "+
            "Values ( #{memId}, #{movieId}, #{textReview}, #{photoReview}, #{rating}, 0 , NOW(), NOW(), #{movieGbn} ) "
    )
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int movieReviewInsrt(Review review);

    @Update(" Update reviews set is_del = 1 where id = #{id}  ")
    public int deleteReview(int id);


    @Update(" Update reviews " +
            " set text_review = #{textReview} , photo_review = #{photoReview} , rating = #{rating}, edit_date = NOW() "+
            " where id = #{id}  ")
    public int updateReview(Review review);


} 