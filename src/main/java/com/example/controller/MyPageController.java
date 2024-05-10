package com.example.controller;

import com.example.domain.Member;

import com.example.groupbuying.dto.BoardDto;
import com.example.groupbuying.service.BoardService;

import com.example.mypage.domain.MyPage;
import com.example.mypage.service.MyPageService;

import com.example.taxi.dto.TaxiDto;
import com.example.taxi.service.TaxiService;

import com.example.session.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.sql.Date;
import java.util.Optional;

@Controller
public class MyPageController {

  private final MyPageService myPageService;
  private final TaxiService taxiService;
  private BoardService boardService;

  public MyPageController(MyPageService myPageService, TaxiService taxiService, BoardService boardService) {
    this.myPageService = myPageService;
    this.taxiService = taxiService;
    this.boardService = boardService;
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

  @GetMapping("/firstmypage")
  public String firstMyPage(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

    Optional<MyPage> myPageOptional = myPageService.findById(loginMember.getId());
    if (loginMember == null) {
      return "redirect:/login";
    }
    model.addAttribute("mypage", myPageOptional.get());
    return "firstmypage";
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

  @GetMapping("/groupbuylist")
  public String groupbuyListPosts(@RequestParam(name = "search", required = false) String search, Model model) {

    List<TaxiDto> taxiDtoList;
    if (search != null && !search.trim().isEmpty()) {
      taxiDtoList = taxiService.searchByTitle(search);
    } else {
      taxiDtoList = taxiService.getTaxiList();
    }

    List<BoardDto> boardDtoList;
    if (search != null && !search.trim().isEmpty()) {
      boardDtoList = boardService.searchByTitle(search);
    } else {
      boardDtoList = boardService.getBoardList();
    }


    model.addAttribute("taxiPostList", taxiDtoList);
    model.addAttribute("boardPostList", boardDtoList);

    return "groupbuylist.html";
  }

  }