package com.example.movie_review.interceptors;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.movie_review.annotations.SessionChecker;
import com.example.movie_review.helpers.WebHelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;

/**
 * Interceptor 클래스
 * 
 * 직접 컨트롤러 역할을 수행하는 클래스는 아님(웹 브라우저로 접근 불가)
 * 
 * 다른 컨트롤러가 브라우저의
 *  1) 받은 직후,
 *  2) 응답을 보내기 직전,
 *  3) 응답을 보낸 직후에
 * 개입하여 컨트롤러보다 먼저 특정 기능을 수행하는 역할을 함
 * 
 * 예) 모든 페이지가 공통으로 수행해야하는 기능(로그인 체크, 공통 데이터 세팅 등)을 
 *     인터셉터가 담당하도록 하여 코드의 중복을 제거할수 있음
 */
@Slf4j
@Component // SpringBoot에게 이 클래스를 스캔해서 Bean으로 등록하라고 알려주는 어노테이션
@RequiredArgsConstructor
public class MyInterceptor implements HandlerInterceptor {
    // 페이지의 실행 시작 시간을 저장할 변수
    long startTime = 0;
    // 페이지의 실행 완료 시간을 저장할 변수
    long endTime = 0;

    // WebHelper 객체
    private final WebHelper webHelper;


    /**
     * 3) 컨트롤러가 응답을 완료한 직후에 자동으로 호출되는 메서드
     *  --> 웹 브라우저에 HTML 응답이 완료된 후에 실행됨
     *  --> DB 연결 해제, 자원 반납 등의 작업을 수행할 수있지만, SpringBoot에서는 자동으로 실행되기 때문에, 거의 사용 안함
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable Exception ex) throws Exception {
        log.debug("3) MyInterceptor.afterCompletion 실행");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * 2) 컨트롤러가 응답을 보내기 직전에 자동으로 호출 되는 메서드
     *  --> 컨트롤러가 return을 해서 View를 호출하면, View가 호출되기 직전에 실행됨
     *  --> 주로 컨트롤러가 View에게 넘겨주는 데이터를 중간에 가로채서 조작하는 용도로 사용 됨
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        // log.debug("2) MyInterceptor.postHandle 실행");

        // (1) 컨토롤러의 실행 종료 시간을 가져옴
        endTime = System.currentTimeMillis();
        // (2) 컨트롤러가 실행하는데 걸린 시간을 가져옴
        log.info(String.format("running time : %d(ms)", endTime - startTime));

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 1) 브라우저가 접속한 직후, 컨트롤러의 특정 메서드가 실행되기 직전에 자동으로 호출되는 메서드
     *  --> 주로 사용되는 메서드
     *  --> 로그인 체크, 공통 데이터 세팅 등의 기능을 수행하는 용도로 사용됨
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    //    log.debug("1) MyInterceptor.preHandle 실행");

        log.info("-----------new client--------------------------");
        // webHelper = new WebHelper(request);
        // (1) 페이지의 실행 시작 시간 구하기
        startTime = System.currentTimeMillis();
        // (2) 접속학 클라이언트(=웹 브라우저) 정보 확인하기
        String ua = request.getHeader("User-Agent");
        Parser uParser = new Parser();
        Client c = uParser.parse(ua);
        // (3) 클라이언트의 IP주소 가져오기
        String ipAddr = webHelper.getClientIp();
        //(4) 로그로 클라이언트 정보 남김
        String fmt = "[Client] %s, %s, %s, %s, %s, %s";
        // 운영체제 버전 정보
        String osVersion = c.os.major + (c.os.minor!= null ? "." +c.os.minor : "");
        // 브라우저 버전 정보
        String uaVersion = c.userAgent.major + (c.userAgent.minor != null ? "."+ c.userAgent.minor :"");
        String clientInfo = String.format(fmt, ipAddr, c.device.family, c.os.family, osVersion, c.userAgent.family, uaVersion);
        log.info(clientInfo);
        //(5) 클라이언트의 요청 정보(url) 확인하기
        // 현재 실행되는 컨트롤러의 url 획득
        String url = request.getRequestURL().toString();
        // GET, POST, PUT, DELETE 중에서 어떤방식으로 접근했는지 조회
        String methodName = request.getMethod();
        // URL에서 ? 이후에 전달되는 QueryString 문자열을 모두 가져온다
        String queryString = request.getQueryString();
        if(queryString != null){
            url = url +"?"+queryString;
        }
        //획득한 정보를 로그로 표시
        log.info(String.format("[%s] %s", methodName,url));
        //(6) 클라이언트가 머물렀던 이전 페이지 확인
        String referer = request.getHeader("referer");
        // 이전에 머물었던 페이지가 존재한다면?
        // 직전 종료시간과 이번 접속 시작시간 과의 차이는 이전 페이지에 머문 시간을 의미
        if(referer != null && endTime >0){
            log.info(String.format("REFERER : time=%.3f(s), url = %s", (startTime-endTime)/1000.0, referer));
        }

        if(handler instanceof HandlerMethod handlerMethod){
            SessionChecker annotation = handlerMethod.getMethodAnnotation(SessionChecker.class);

            if(annotation != null){
                boolean enable = annotation.enable();

                HttpSession session = request.getSession();
                boolean isLoggedIn = session != null && session.getAttribute("memberInfo") != null;

                if(enable){
                    if(!isLoggedIn){
                        throw new AccessDeniedException("로그인이 필요합니다.");
                    }
                }else{
                    if(isLoggedIn){
                        throw new AccessDeniedException("로그인 중에는 접근할 수 없습니다.");
                    }
                }
            }
        }

       
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    
}
