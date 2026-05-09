package com.example.movie_review.annotations;

import java.lang.annotation.*;

/**
 * 
 * 로그인 체크를 수행해야 하는 페이지 인지 여부를 설정하는 어노테이션
 * enable() 메서드를 통해 boolean 값을 메서드 단위로 설정 가능
 * 이 어노테이션이 적용된 메서드는 Interceptor 를 통해 세션 체크를 수행함
 * 
 * 
 * enable = true  --> 로그인을 해야만 접근 가능한 페이지
 * enable = false --> 로그인을 하지 않아야만 접근 가능한 페이지
 */

@Target({ElementType.METHOD})// 이 어노테이션은 메서드에만 붙일수 있음을 지정
@Retention(RetentionPolicy.RUNTIME) // 실행 중에도 이 어노테이션 정보가 유지되도록 설정
public @interface SessionChecker { // 사용자 정의 어노테이션
    boolean enable() default true;
    
}
