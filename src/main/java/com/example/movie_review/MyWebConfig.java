package com.example.movie_review;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.movie_review.interceptors.MyInterceptor;

import lombok.RequiredArgsConstructor;

/**
 * @Controller , @Service, @Mapper 등 SpringBoot에 자동으로 등록되는 Bean 외에
 * 자동으로 등록되지 않는 Bean들을 수동으로 등록하기 위한 설정 클래스
 */

@Configuration
@RequiredArgsConstructor
public class MyWebConfig implements WebMvcConfigurer{

    private final MyInterceptor myInterceptor;

    //업로드 된 파일이 저장될 경로(application.properties로부터 읽어옴)
    @Value("${upload.dir}")
    private String uploadDir;
    // 업로드된 파일이 노출될 URL 경로 (application.properties로부터 읽어옴)
    @Value("${upload.url}")
    private String uploadUrl;

    /**
     * 인터셉터를 등록하는 메서드
     * 파라미터로 전달되는 registry 객체에 내가 만든 인터셉터를 등록해야함
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    
        // 직접 정의한 MyInterceptor 클래스를 인터셉터로 등록
       InterceptorRegistration ir = registry.addInterceptor(myInterceptor);
        // 해당 경로는 인터셉터가 가로채지 않는다.
       ir.excludePathPatterns("/error", "/robots.txt", "/favicon.ico","/assets/**");
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){

        // URL에 포함되어 있는 /files 라는 주소를
        // 실제 파일이 저장되어있는 "/D:/workspace/mega_springboot/upload" 폴더와 매핑시킴
        registry.addResourceHandler(String.format("%s/**", uploadUrl))
                .addResourceLocations(String.format("file://%s/", uploadDir));
    }
}
