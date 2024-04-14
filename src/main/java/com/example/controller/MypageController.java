package com.example.controller;

import com.example.domain.Member;
import com.example.mypage.domain.MyPage;
import com.example.mypage.service.MyPageService;
import com.example.session.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.Optional;

@Controller
public class MyPageController {

  private final MyPageService myPageService;

  public MyPageController(MyPageService myPageService) {
    this.myPageService = myPageService;
  }

  @GetMapping("/mypage")
  public String homeLogin(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
    if (loginMember == null) {
      return "home";
    }


    Optional<MyPage> myPageOptional = myPageService.findById(loginMember.getId());
    if (!myPageOptional.isPresent()) {
      return "home";
    }


    model.addAttribute("mypage", myPageOptional.get());
    return "firstmypage";
  }

  @GetMapping("/mypage/info")
  public String showMyPageInfo(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
    if (loginMember == null) {
      return "redirect:/login";
    }

    Optional<MyPage> myPageOptional = myPageService.findById(loginMember.getId());
    if (!myPageOptional.isPresent()) {
      return "redirect:/firstmypage";
    }

    model.addAttribute("mypage", myPageOptional.get());
    return "mypage";
  }


  @PostMapping("/mypage/update")
  public String updateMyPage(@ModelAttribute MyPage myPage,  @RequestParam("password") String password, RedirectAttributes redirectAttributes) {

    Long id = myPage.getId();
    String email = myPage.getEmail();
    String phoneNumber = myPage.getPhonenumber();
    String bank = myPage.getBank();
    String account = myPage.getAccount();
    Date birthday = myPage.getBirthday();

    myPageService.updateMemberInfo(id, email, phoneNumber, bank, account , birthday, password);


    redirectAttributes.addFlashAttribute("successMessage", "Your information has been updated successfully!");


    return "redirect:/mypage/info";
  }


  @GetMapping("/editmypage")
  public String editMyPageForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
    if (loginMember == null) {
      return "redirect:/login";
    }

    Optional<MyPage> myPageOptional = myPageService.findById(loginMember.getId());
    myPageOptional.ifPresent(myPage -> model.addAttribute("mypage", myPage));

    return "editmypage";
  }

}