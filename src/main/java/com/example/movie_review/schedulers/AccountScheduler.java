package com.example.movie_review.schedulers;

import java.util.List;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.movie_review.helpers.FileHelper;
import com.example.movie_review.models.Member;
import com.example.movie_review.services.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableAsync
@RequiredArgsConstructor
public class AccountScheduler {

    private final MemberService memberService;

    @Scheduled(cron = "0/5 * * * * ?")// 5초마다 실행
    public void processOutMembers() throws  Exception{
        log.debug("탈퇴 회원 정리 시작");

        List<Member> outMembers = null;

        try {
            log.debug("탈퇴 회원 조회 및 삭제");
            outMembers = memberService.processOutMembers();
        } catch (Exception e) {
            log.error("탈퇴 회원 조회 및 삭제 실패",e);
            return;
        }

        if(outMembers == null || outMembers.size() == 0){
            log.debug("탈퇴 대상 없음 ");;
            return;
        }

        
    }
    
}
