package com.example.movie_review.controllers.apis.account;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.movie_review.annotations.SessionChecker;
import com.example.movie_review.exceptions.StringFormatException;
import com.example.movie_review.helpers.RegexHelper;
import com.example.movie_review.models.Member;
import com.example.movie_review.services.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequiredArgsConstructor
public class AccountRestController {
    
    private final MemberService memberService;
    private final RegexHelper regexHelper;


    @GetMapping("/api/account/id_unique_check")
    @SessionChecker(enable = false)
    public Map<String, Object> isUniqueIdCheck(@RequestParam("user_id") String userId)throws Exception{

        regexHelper.isValue(userId, "아이디를 입력하세요.");
        regexHelper.isEngNum(userId, "아이디는 영문자와 숫자만 입력 가능합니다.");

        Member input = new Member();
        input.setUserId(userId);

        memberService.isUniqueUserId(input); 
        
        Map<String, Object> result = new HashMap<>();
        result.put("result", true); // 또는 true/false

        return result;
    }

    @GetMapping("/api/account/email_unique_check")
    @SessionChecker(enable = false)
    public Map<String, Object> isUniqueEmailCheck(@RequestParam("email") String email)throws Exception{

        regexHelper.isValue(email, "이메일을 입력하세요.");
        regexHelper.isEmail(email, "이메일 형식이 일치하지 않습니다.");

        Member input = new Member();
        input.setEmail(email);

        memberService.isUniqueEmail(input);

        Map<String, Object> result = new HashMap<>();
        result.put("result", true); // 또는 true/false

        return result;
    }

    @PostMapping("/api/account/join")
    @SessionChecker(enable = false)
    public Map<String, Object> join(
        @RequestParam("user_id") String userId,
        @RequestParam("user_pw") String userPw,
        @RequestParam("user_pw_re") String userPwRe,
        @RequestParam("user_name") String userName,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone
    )throws Exception{
        regexHelper.isValue(userId, "아이디를 입력하세요.");
        regexHelper.isEngNum(userId, "아이디는 영문자와 숫자만 입력가능 합니다.");
        regexHelper.isValue(userPw, "비밀번호를 입력하세요.");

        if(!userPw.equals(userPwRe)){
            throw new StringFormatException("비밀번호 확인이 잘못되었습니다.");
        }

        regexHelper.isValue(userName, "이름을 입력하세요.");
        regexHelper.isKor(userName, "이름은 한글만 입력가능합니다.");
        regexHelper.isValue(email, "이메일을 입력하세요.");
        regexHelper.isEmail(email, "이메일 형식이 잘못되었습니다.");
        regexHelper.isPhone(phone, "전화번호 형식이 잘못되었습니다.");
    
        Member input = new Member();
        input.setUserId(userId);
        input.setUserPw(userPw);
        input.setUserName(userName);
        input.setEmail(email);
        input.setPhone(phone);

        memberService.joinMember(input);
        
        Map<String, Object> result = new HashMap<>();
        result.put("result", "OK");

        return result;

    }

    @PostMapping("/api/account/login")
    @SessionChecker(enable = false)
    public Map<String, Object> login( 
        HttpServletRequest request,
        @RequestParam("user_id") String userId,
        @RequestParam("user_pw") String userPw,
        @RequestParam(value  ="redirect" ,required = false) String redirect)throws Exception
    {
        regexHelper.isValue(userId, "아이디를 입력하세요.");
        regexHelper.isEngNum(userId, "아이디는 영문자와 숫자만 입력가능 합니다.");
        regexHelper.isValue(userPw, "비밀번호를 입력하세요.");

        Member input = new Member();
        input.setUserId(userId);
        input.setUserPw(userPw);

        Member output = memberService.loginMember(input);
        
        request.getSession().setAttribute("memberInfo", output);

        Map<String, Object> result = new HashMap<>();
        result.put("result", "OK");
        result.put("redirect", redirect);

        return result;
    }

    @GetMapping("/api/account/logout")
    @SessionChecker(enable = true)
    public  Map<String, Object> logout(HttpServletRequest request) {
        
        HttpSession session = request.getSession();

        session.invalidate();
        
        Map<String, Object> result = new HashMap<>();
        result.put("result", "OK");

        return result;
    }
    
    @PutMapping("/api/account/edit")
    @SessionChecker(enable = true)
    public Map<String, Object> edit(
        HttpServletRequest request,                                     // 세션 갱신용
        @SessionAttribute("memberInfo") Member memberInfo,              // 현재 세션 정보 확인용
        @RequestParam("user_pw") String userPw,
        @RequestParam("new_user_pw") String newUserPw,                
        @RequestParam("new_user_pw_confirm") String newUserPwConfirm,  
        @RequestParam("user_name") String userName,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone
    )throws Exception{
        /** 1) 입력값에 대한 유효성 검사 */
        regexHelper.isValue(userPw, "현재 비밀번호를 입력하세요.");

        if ((newUserPw != null && !newUserPw.isEmpty()) && !newUserPw.equals(newUserPwConfirm)) {
            // 신규 비밀번호가 입력된 경우
            regexHelper.isValue(newUserPw, "비밀번호 확인이 잘못되었습니다.");
        }

        regexHelper.isValue(userName, "이름을 입력하세요.");
        regexHelper.isKor(userName, "이름은 한글만 입력할 수 있습니다.");
        regexHelper.isValue(email, "이메일을 입력하세요.");
        regexHelper.isEmail(email, "이메일 형식이 잘못되었습니다.");
        regexHelper.isValue(phone, "전화번호를 입력하세요.");
        regexHelper.isPhone(phone, "전화번호 형식이 잘못되었습니다.");
       

        /** 2) 이메일 중복 검사 */
        Member input = new Member();
        input.setEmail(email);
        input.setId(memberInfo.getId());
        memberService.isUniqueEmail(input);
    
        Member output = new Member();
        output.setId(memberInfo.getId());
        output.setUserId(memberInfo.getUserId());
        output.setUserName(userName);
        output.setUserPw(userPw);
        output.setNewUserPw(newUserPw);
        output.setEmail(email);
        output.setPhone(phone);

        memberService.updatMember(output);
        request.getSession().setAttribute("memberInfo", output);

        Map<String, Object> result = new HashMap<>();
        result.put("result", "OK");

        return result;

    }

    @DeleteMapping("/api/account/out")
    @SessionChecker(enable = true)
    public  Map<String, Object> out(HttpServletRequest request,
        @SessionAttribute("memberInfo") Member memberInfo,
        @RequestParam("user_pw") String password) throws Exception{

            memberInfo.setUserPw(password);

            memberService.out(memberInfo);

            HttpSession session = request.getSession();
            session.invalidate();

            Map<String, Object> result = new HashMap<>();
            result.put("result", "OK");

            return result;
    }

    @PostMapping("/api/account/find_id")
    @SessionChecker(enable = false)
    public Map<String, Object> findId( 
        @RequestParam("user_name") String userName,
        @RequestParam("email") String email)throws Exception
    {
        regexHelper.isValue(userName, "이름을 입력하세요.");
        regexHelper.isKor(userName, "이름은 한글만 입력할 수 있습니다.");
        regexHelper.isValue(email, "이메일을 입력하세요.");
        regexHelper.isEmail(email, "이메일 형식이 잘못되었습니다.");

        Member input = new Member();
        input.setUserName(userName);
        input.setEmail(email);

        Member output = memberService.findIdMember(input);

        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("user_id", output.getUserId());

        return result;
    }

    @PostMapping("/api/account/find_pw")
    @SessionChecker(enable = false)
    public Map<String, Object> findPw(
            @RequestParam("user_id") String userId,
            @RequestParam("email") String email
    ) throws Exception {

        // 1. 유효성 검사
        regexHelper.isValue(userId, "아이디를 입력하세요.");
        regexHelper.isValue(email, "이메일을 입력하세요.");
        regexHelper.isEmail(email, "이메일 형식이 잘못되었습니다.");

        // 2. Service 호출 
        Member input = new Member();
        input.setUserId(userId);
        input.setEmail(email);

        memberService.sendResetPwLink(input);

        // 3️⃣ 응답
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("result", "OK");

        return result;
    }

    @PostMapping("/api/account/reset_pw_confirm")
    @SessionChecker(enable = false)
    public Map<String, Object> resetPwConfirm(
            @RequestParam("token") String token,
            @RequestParam("new_pw") String new_pw
    ) throws Exception {

        memberService.resetPasswordByToken(token, new_pw);

        return Map.of("result", "OK");
    }
    
}
