package com.example.mypage.service;

import com.example.mypage.domain.MyPage;
import com.example.mypage.repository.MyPageRepository;
import com.example.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageService {

  private final MyPageRepository myPageRepository;

  @Transactional(readOnly = true)
  public Optional<MyPage> findById(Long id) {
    return myPageRepository.findById(id);
  }

  @Transactional
  public void updateMemberInfo(Long id, String email, String phonenumber, String bank, String account, Date birthday, String password  ) {
    myPageRepository.updateMemberInfo(id, email, phonenumber, bank, account,  birthday, password);
  }

}
