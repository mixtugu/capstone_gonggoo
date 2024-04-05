  package com.example.mypage.domain;

  import com.example.domain.Member;
  import lombok.Getter;
  import lombok.Setter;

  import jakarta.persistence.*;
  import java.sql.Date;

  @Entity
  @Getter @Setter
  public class MyPage {

    @Id
    private Long id;

    private String phonenumber;

    private Date birthday;

    private String email;

    private int point;

    private String payment;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

  }
