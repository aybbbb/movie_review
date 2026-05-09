package com.example.movie_review.helpers;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MailHelper {
    
    private final JavaMailSender javaMailSender;

    //환경설정 파일에 명시된 값을 변수에 할당
    private String senderName;
    private String senderEmail;

    @Autowired
    public MailHelper(JavaMailSender javaMailSender, 
            @Value("${mailhelper.sender.name}") String senderName,
            @Value("${mailhelper.sender.email}") String senderEmail)
    {
        this.javaMailSender = javaMailSender;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
    }


    public void sendMail(String receiverName, String receiverEmail, String subject, String content) throws Exception{
         log.debug("----------------------------------------------");
        log.debug("MailHelper() 실행");
        log.debug(String.format("Sender  : %s <%s>",senderName, senderEmail));
        log.debug(String.format("receiverEmail : %s <%s>",receiverName, receiverEmail));
        log.debug(String.format("subject : %s",subject));
        // log.debug(String.format("content : %s",content));
        log.debug("----------------------------------------------");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

        try {
            helper.setSubject(subject);
            helper.setText(content,true);
            helper.setTo(new InternetAddress(receiverEmail, receiverName,StandardCharsets.UTF_8.name()));

            // 발신자 이름은 임의로 설정 가능하지만,
            // 발신 주소는 반드시 application.properties 에 설정된 계정이어야함
            helper.setFrom(new InternetAddress("ayk950308@gmail.com","메가스터디",StandardCharsets.UTF_8.name()));

            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("메일 발송 중 오류 발생 ",e);
        }
    }


}
