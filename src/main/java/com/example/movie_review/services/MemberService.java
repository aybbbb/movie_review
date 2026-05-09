package com.example.movie_review.services;

import java.util.List;

import com.example.movie_review.models.Member;


public interface MemberService {
    
    public Member joinMember(Member input) throws Exception;
    public boolean isUniqueEmail(Member input) throws Exception;
    public boolean isUniqueUserId(Member input) throws Exception;
    public boolean out(Member input) throws Exception;
    public Member loginMember(Member input) throws Exception;
    public Member updatMember(Member input) throws Exception;
    public Member findIdMember(Member input) throws Exception;
    public void sendResetPwLink(Member input) throws Exception;
    public void resetPasswordByToken(String token, String newPw) throws Exception;
    public List<Member> processOutMembers() throws Exception;
}
