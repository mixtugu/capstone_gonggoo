package com.example.controller;

import com.example.community.dto.CroomDto;
import com.example.community.service.CroomService;
import com.example.domain.Member;

import com.example.groupbuying.dto.BoardDto;
import com.example.groupbuying.service.BoardService;

import com.example.groupbuying.service.ParticipantService;
import com.example.mypage.domain.MyPage;
import com.example.mypage.service.MyPageService;

import com.example.taxi.dto.TaxiDto;
import com.example.taxi.service.TaxiParticipantService;
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
  private final TaxiParticipantService taxiParticipantService;
  private final ParticipantService participantService;
  private final CroomService croomService;

  public MyPageController(MyPageService myPageService, TaxiParticipantService taxiParticipantService, ParticipantService participantService, CroomService croomService) {

    this.myPageService = myPageService;
    this.taxiParticipantService = taxiParticipantService;
    this.participantService = participantService;
    this.croomService = croomService;
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
  public String groupbuyListPosts(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
    if (loginMember == null) {
      return "redirect:/login";
    }


    List<TaxiDto> taxiDtoList = taxiParticipantService.getParticipatedTaxiListByMemberId(loginMember.getId());
    List<BoardDto> boardDtoList = participantService.getParticipatedBoardListByMemberId(loginMember.getId());
    List<CroomDto> croomDtoList = croomService.getParticipatedCroomListByMemberId(loginMember.getId());

    model.addAttribute("taxiPostList", taxiDtoList);
    model.addAttribute("boardPostList", boardDtoList);
    model.addAttribute("croomPostList", croomDtoList);

    return "groupbuylist.html";
  }

  }