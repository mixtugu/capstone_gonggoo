package com.example.notice.service;

import com.example.notice.domain.Notice;
import com.example.notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

  @Autowired
  private NoticeRepository noticeRepository;

  @Transactional
  public List<Notice> getAllNotices() {
    return noticeRepository.findAll();
  }

  public Notice getNoticeById(Long id) {
    Optional<Notice> notice = noticeRepository.findById(id);
    return notice.orElse(null);
  }

}
