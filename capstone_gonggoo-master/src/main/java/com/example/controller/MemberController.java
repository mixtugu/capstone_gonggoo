package com.example.controller;

import com.example.domain.Member;
import com.example.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.persistence.Table;

@Controller
@RequiredArgsConstructor
@Table(name = "member")
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member) {
        return "members/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Validated @ModelAttribute Member member, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }

        memberService.join(member);

        return "redirect:/";
    }
}
