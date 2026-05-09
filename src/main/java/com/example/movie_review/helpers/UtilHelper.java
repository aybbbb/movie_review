package com.example.movie_review.helpers;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class UtilHelper {
    public String getRandomString(int length){
        if(length <1){
            throw new IllegalArgumentException("길이는 1자리 이상이어야합니다.");
        }

        final String DATA = "abcdefghijklmnopqrstuvwxyzABCEDFGHIJKLMNOPQRSTUVWXYZ0123456789";

        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int rnd = random.nextInt(DATA.length());

            sb.append(DATA.charAt(rnd));
        }

        return sb.toString();
    }
}
