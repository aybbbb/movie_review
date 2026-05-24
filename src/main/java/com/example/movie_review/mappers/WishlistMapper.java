package com.example.movie_review.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WishlistMapper {
    
    @Select(" SELECT is_del From wishlist Where mem_id = #{memId} and movie_id = #{movieId} and movie_gbn = #{movieGbn} ")
    public Integer getWishlistId(@Param("memId") int memId, @Param("movieId")  String movieId, @Param("movieGbn")  String movieGbn );

    @Insert(" INSERT INTO wishlist (mem_id, movie_id, is_del, reg_date, edit_date, movie_gbn, movie_title, movie_poster ) " + 
            " VALUES (#{memId}, #{movieId}, 0, NOW(), NOW(), #{movieGbn}, #{movieTitle}, #{moviePoster}) " + 
            " ON DUPLICATE KEY " +
            " UPDATE  is_del = 1 - is_del , edit_date =NOW() ")
    public int toggleWishlist(@Param("memId") int memId, @Param("movieId") String movieId, @Param("movieGbn") String movieGbn, @Param("movieTitle") String movieTitle, @Param("moviePoster") String moviePoster);
}
