package com.example.movie_review.exceptions;

/**
 * 업무에서 정의한 처리 불가능한 경우를 예외로 발생시키기 위한 클래스
 */
public class ServiceNoResultException extends Exception {
    public ServiceNoResultException(String message) {
        super(message);
    }
}
