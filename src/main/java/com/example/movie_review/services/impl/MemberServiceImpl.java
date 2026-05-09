package com.example.movie_review.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.movie_review.exceptions.AlreadyExistsException;
import com.example.movie_review.exceptions.ServiceNoResultException;
import com.example.movie_review.helpers.FileHelper;
import com.example.movie_review.helpers.MailHelper;
import com.example.movie_review.mappers.MemberMapper;
import com.example.movie_review.models.Member;
import com.example.movie_review.services.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberMapper memberMapper;

    private final MailHelper mailHelper;
    private final FileHelper fileHelper;

    @Override
    public Member joinMember(Member input) throws Exception {
        Member tmp1 = new Member();
        tmp1.setUserId(input.getUserId());
        this.isUniqueUserId(tmp1);

        Member tmp2 = new Member();
        tmp2.setEmail(input.getEmail());
        this.isUniqueEmail(tmp2);

        if(memberMapper.insert(input) == 0){
            throw new ServiceNoResultException("저장된 회원이 없습니다.");
        }

        return memberMapper.selecMember(input);
    }

    @Override
    public boolean isUniqueEmail(Member input) throws Exception {
        if(memberMapper.SelectCount(input) > 0){
            throw new AlreadyExistsException("사용할수 없는 이메일 입니다.");
        }
        return true;
    }

    @Override
    public boolean isUniqueUserId(Member input) throws Exception {
        if(memberMapper.SelectCount(input) > 0){
            throw new AlreadyExistsException("사용할수 없는 아이디 입니다.");
        }

        return true;
    }

    @Override
    public Member loginMember(Member input) throws Exception {

        Member output = memberMapper.login(input);

        if(output == null){
            throw new ServiceNoResultException("아이디 또는 비밀번호가 일치하지 않습니다."); 
        }

        memberMapper.updateLoginDate(output);

        return output;
    }

    @Override
    public Member updatMember(Member input) throws Exception {
        
       if(memberMapper.update(input) == 0){
            throw new ServiceNoResultException("현재 비밀번호를 확인하세요.");
       }

       return memberMapper.selecMember(input);
    }

    @Override
    public boolean out(Member input) throws Exception {
        if(memberMapper.out(input)==0){
            throw new ServiceNoResultException("현재 비밀번호를 확인하세요.");
        }
        return true;
    }

    @Override
    public Member findIdMember(Member input) throws Exception {
        
        if(memberMapper.findId(input)== null){
            throw new ServiceNoResultException("조회된 회원이 없습니다.");
        }
        return memberMapper.findId(input);
    }

    @Override
    public void sendResetPwLink(Member input) throws Exception {

        // 1. 회원 조회
        if(memberMapper.SelectCount(input) == 0){
            throw new AlreadyExistsException("일치하는 회원이 없습니다.");
        }
        
        // 2. 토큰 생성
        String token = UUID.randomUUID().toString();

        // 3. 토큰 저장
        Member tokenInput = new Member();
        tokenInput.setUserId(input.getUserId());
        tokenInput.setToken(token);

        memberMapper.insertToken(tokenInput);

        // 4. 링크 생성
        String link = "http://localhost:8080/account/reset_pw_form?token=" + token;

        // 5. 템플릿 읽기
        ClassPathResource resource = new ClassPathResource("mail_templates/reset_pw.html");
        String template = fileHelper.readString(resource.getFile().getAbsolutePath());

        // 6️⃣ 치환 ⭐
        template = template.replace("{{userId}}", input.getUserId());
        template = template.replace("{{link}}", link);

        // 7️⃣ 메일 발송
        String subject = "[비밀번호 재설정 안내]";
        mailHelper.sendMail(input.getUserName(), input.getEmail(), subject, template);

    }

    @Override
    public void resetPasswordByToken(String token, String newPw) throws Exception {

        // 1. 토큰 검증
        Member member = memberMapper.findByToken(token);

        if (member == null) {
            throw new ServiceNoResultException("유효하지 않은 토큰입니다.");
        }

        // 2. 이미 사용된 토큰인지 체크
        if (memberMapper.isUsedToken(token) > 0) {
            throw new ServiceNoResultException("이미 사용된 링크입니다.");
        }

        // 3. 비밀번호 변경
        Member input = new Member();
        input.setUserId(member.getUserId());
        input.setUserPw(newPw);

        memberMapper.updatePassword(input);

        // 4. 토큰 사용 처리 (중요 ⭐)
        memberMapper.updatUseToken(token);
    }

    @Override
    public List<Member> processOutMembers() throws Exception {
        
        List<Member> output = null;

        memberMapper.deleteOutMembers();
        
        return output;
    }

    
}
