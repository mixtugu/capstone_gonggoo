package com.example.domain;


import com.example.groupbuying.domain.entity.Participant;
import lombok.Builder;
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MYPAGE_ID") // 외래키 이름을 명시
    private MyPage myPage;

    public Member() {

    }

    @Builder
    public Member(Long id, String loginId, String password, String name, MyPage myPage) {
        this .id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.myPage = myPage;
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.setMember(this);
    }
}
