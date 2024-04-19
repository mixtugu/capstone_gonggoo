package com.example.notice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter @Setter
public class Notice {

  @Id
  @GeneratedValue
  @Column(name = "NOTICE_NUM")
  private int noticeNum;

  private String noticeTitle;

  private String noticeContent;

  private Date noticeDate;
}
