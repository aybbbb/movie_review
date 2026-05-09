package com.example.movie_review.models;

import lombok.Data;

@Data
public class PasswordResetToken {
    
    int id;
    String userId;
    String token;
    String expireAt;
    String isUsed;
    String regDate;
}
