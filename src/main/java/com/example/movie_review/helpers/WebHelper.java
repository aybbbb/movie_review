package com.example.movie_review.helpers;

import java.io.PrintWriter;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 웹 관련 기능 구현시 필요한 기능을 제공하는 Helper 클래스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebHelper {

    // 브라우저로부터 전달된 정보를 담고 있는 requests 객체
    // 내가 구현하는 클래스 이 객체를 스스로 만들수 없기 때문에
    // 외부에서 이 객체를 전달받도록 처리해야함

    private final HttpServletRequest request;

    private final HttpServletResponse response;

  
    /**
     * 클라이언트의 IP 주소를 가져오는 메서드
     * 여러가지 방법으로 시도하고 가장 적합한 IP 주소를 반환
     * 
     * @param request HttpServletRequset 객체
     * @return IP 주소
     */
    public String getClientIp(){
        String ip = request.getHeader("X-Forwarded-For");

        if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equals(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equals(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equals(ip)){
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equals(ip)){
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equals(ip)){
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public void redirect(int statusCode, String url, String message) throws Exception{
        response.setStatus(statusCode);
        response.setContentType("text/html; charset=UTF-8");

        // HTML 코드를 직접 출력할수 있는 PrintWriter 객체 생성
        PrintWriter out = response.getWriter();

        out.println("<DOCTYPE html>");
        out.println("<html lang='ko'");
        out.println("<head>");
        out.println("<script>");

        if(message != null && !message.isEmpty()){
            out.println("alert('"+message+"');");
        }

        if(url != null && !url.isEmpty()){
            out.println("window.location.replace('"+url+"');");
        }else{
            out.println("history.back();");
        }

        out.println("</script>");
        out.println("</head>");
        out.println("<body></body>");
        out.println("</html>");

        // 출력한 내용을 클라이언트로 전송
        out.flush();
    }
    
    public void redirect(String url, String message) throws Exception{
        this.redirect(200, url, message);
    }

    public void redirect(String url)throws Exception{
        this.redirect(200, url, null);
    }

    public void badRequset(Exception e) throws Exception{
        log.error("[403] BadRequset Error ::: {}",e.getMessage(),e);
        this.redirect(403, null, e.getMessage());
    }

    public void serverError(Exception e) throws Exception{
        String message = e.getMessage().trim().replace("'", "\\'").split(System.lineSeparator())[0];
        log.error("[500] Server Error ::: {}",e.getMessage(),e);
        this.redirect(500, null, message);
    }
}
