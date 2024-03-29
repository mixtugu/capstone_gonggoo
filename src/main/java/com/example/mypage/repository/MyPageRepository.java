package com.example.mypage.repository;

import com.example.mypage.domain.MyPage;
import com.example.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyPageRepository {

  private final EntityManager em;

  public void save(MyPage myPage) {
    em.persist(myPage);
  }
  public Optional<MyPage> findById(Long id) {
    MyPage myPage = em.createQuery("select m from MyPage m where m.id = :id", MyPage.class)
        .setParameter("id", id)
        .getSingleResult();
    return Optional.ofNullable(myPage);
  }

  public void updateMemberInfo(Long id, String email, String phonenumber, String payment, Date birthday, String password) {
    MyPage myPage = em.find(MyPage.class, id);
    if (myPage != null) {
      myPage.setEmail(email);
      myPage.setPhonenumber(phonenumber);
      myPage.setPayment(payment);
      myPage.setBirthday(birthday);
      myPage.getMember().setPassword(password);

    }
  }
}
