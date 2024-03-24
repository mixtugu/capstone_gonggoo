package com.example.domain;

import com.example.groupbuying.domain.entity.Participant;
import lombok.Builder;
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

    public Member() {

    }

    @Builder
    public Member(Long id, String loginId, String password, String name) {
        this .id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.setMember(this);
    }
}
