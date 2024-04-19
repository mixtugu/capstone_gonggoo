package com.example.controller;

import com.example.notice.domain.Notice;
import com.example.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class NoticeController {

  @Autowired
  private NoticeService noticeService;


  @GetMapping("/notice")
  public String showNotices(Model model) {
    List<Notice> notice = noticeService.getAllNotices();
    model.addAttribute("notice", notice);
    return "notice";
  }

  @GetMapping("/notice/{id}")
  public String showNoticeDetail(@PathVariable Long id, Model model) {
    Notice notice = noticeService.getNoticeById(id);
    model.addAttribute("notice", notice);
    return "noticeall";
  }
}
