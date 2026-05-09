package com.example.movie_review.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.movie_review.models.Member;


@Mapper
public interface MemberMapper {

    @Select("<script> "+
            " SELECT COUNT(*) FROM MEMBERS "+
            "   <where> "+
            "    <if test='userId != null'> user_id =#{userId} </if> "+
            "    <if test='email != null'> and email =#{email} </if> "+
            " </where>"+
            "</script>"
    )
    public int SelectCount(Member input);

    @Insert(" INSERT INTO MEMBERS "+
            "     (user_id, user_pw, user_name, email, phone, is_out, is_admin, login_date, reg_date, edit_date )"+
            " VALUES ( #{userId}, MD5(#{userPw}), #{userName}, #{email}, #{phone}, 'N', 'N', NULL, NOW(), NOW() ) "
    )
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int insert(Member input);

    @Select(" SELECT user_id, user_pw, user_name, email, phone, is_out, is_admin, login_date, reg_date, edit_date "+
            " FROM MEMBERS "+
            " WHERE id = #{id}"
    )
    // 조회 결과를 resultMap 이라는 이름의 Member 객체로 매핑한다
    @Results(id = "resultMap")
    public Member selecMember(Member input);

    @Select(" SELECT id, user_id, user_pw, user_name, email, phone, is_out, is_admin, login_date, reg_date, edit_date "+
            " FROM MEMBERS "+
            " WHERE user_id = #{userId} and user_pw =MD5(#{userPw}) and is_out ='N' "
    )
    // 조회 결과를 resultMap 이라는 이름의 Member 객체로 매핑한다
    @ResultMap("resultMap")
    public Member login(Member input);

    @Update("UPDATE members SET login_date = NOW() WHERE id = #{id} and is_out ='N' ")
    public int updateLoginDate(Member input);

    @Update("<script>" +
            " Update members "+
            " SET user_name = #{userName}, "+
            "     <if test='newUserPw != null and newUserPw != \"\"'> user_pw = MD5(#{newUserPw}), </if> "+
            "     email = #{email} ,"+
            "     phone = #{phone} ,"+
            "     edit_date = Now() "+
            " WHERE  id = #{id} AND user_pw = MD5(#{userPw}) and is_out ='N' "+
            " </script>"
    )
    public int update(Member input);
    

    @Update("UPDATE members SET is_out = 'Y' WHERE id = #{id} AND user_pw = MD5(#{userPw}) and is_out ='N' ")
    public int out(Member input);

    @Delete("Delete from members where is_out ='Y' and edit_date <  DATE_ADD (NOW(), interval -5 minute)  ")
    public int deleteOutMembers();

    @Select(" SELECT user_id "+
            " FROM members "+
            " WHERE user_name = #{userName} AND email = #{email}  and is_out ='N' "
    )
    // 조회 결과를 resultMap 이라는 이름의 Member 객체로 매핑한다
    @ResultMap("resultMap")
    public Member findId(Member input);

    // 비밀번호 재발급
    @Insert(" INSERT INTO password_reset_tokens (user_id, token, expire_at) " +
            " VALUES (#{userId}, #{token}, DATE_ADD(NOW(), INTERVAL 30 MINUTE)) ")
    int insertToken(Member input);

    @Select(" SELECT user_id FROM password_reset_tokens WHERE token = #{token} AND expire_at > NOW()AND is_used = 'N'")
    Member findByToken(String token);

    @Select("SELECT COUNT(*) FROM password_reset_tokens WHERE token = #{token} AND is_used = 'Y' ")
    int isUsedToken(String token);

    @Update("UPDATE password_reset_tokens SET is_used = 'Y' WHERE token = #{token} ")
    int updatUseToken(String token);

    @Update("UPDATE members  SET user_pw = MD5(#{userPw})  WHERE user_id = #{userId}  AND is_out = 'N' ")
    int updatePassword(Member input);

}
