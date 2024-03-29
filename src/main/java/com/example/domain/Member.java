package com.example.domain;

import com.example.mypage.domain.MyPage;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @NotBlank
//    @Size(min = 5,max = 30)
    private String loginId;

    @NotBlank
//    @Size(min = 5,max = 30)
    private String password;

    @NotBlank
//    @Size(min = 3, max = 20)
    private String name;


}
