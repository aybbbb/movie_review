package com.example.movie_review.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class AccountController {
    
    @GetMapping("/account/join")
    public String join() {
        return "account/join";
    }

    @GetMapping("/account/join_result")
    public String join_result() {
        return "account/join_result";
    }

    @GetMapping("/account/login")
    public String login() {
        return "account/login";
    }

    @GetMapping("/account/edit")
    public String editMember() {
        return "account/edit";
    }

    @GetMapping("/account/out")
    public String outMember() {
        return "account/out";
    }

    @GetMapping("/account/find_id")
    public String findId() {
        return "account/find_id";
    }

    @GetMapping("/account/find_pw")
    public String findPw() {
        return "account/find_pw";
    }
    
    @GetMapping("/account/reset_pw_form")
    public String resetPwForm(@RequestParam("token") String token, Model model) {

        model.addAttribute("token", token);

        return "account/reset_pw_form";
    }
    
    
}
