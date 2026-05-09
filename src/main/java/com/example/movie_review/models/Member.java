package com.example.movie_review.models;

import java.io.Serializable;

import lombok.Data;

// 세션에 저장할 클래스 타입은 Serializable 인터페이스 상속해야한다.
@Data
public class Member implements Serializable {
    int id;
    String userId;
    String userPw;
    String newUserPw;
    String userName;
    String email;
    String phone;
    String isOut;
    String isAdmin;
    String loginDate;
    String regDate;
    String editDate;

    //비밀번호 재발급
    String token;
}
