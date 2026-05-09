package com.example.movie_review.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WishlistMapper {
    
    @Select(" SELECT is_del From wishlist Where mem_id = #{memId} and movie_id = #{movieId} ")
    public Integer getWishlistId(@Param("memId") int memId, @Param("movieId")  String movieId );

    @Insert(" INSERT INTO wishlist (mem_id, movie_id, is_del, reg_date, edit_date) " + 
            " VALUES (#{memId}, #{movieId}, 0, NOW(), NOW()) " + 
            " ON DUPLICATE KEY " +
            " UPDATE  is_del = 1 - is_del , edit_date =NOW() ")
    public int toggleWishlist(@Param("memId") int memId, @Param("movieId") String movieId);
}
