package com.example.community.domain.entity;

import com.example.domain.Member;
import com.example.domain.RoomMember;
import com.example.community.domain.entity.Croom;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Cparticipant extends RoomMember {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "croom_id")
    private Croom croom;

    private LocalDateTime joinedAt = LocalDateTime.now();//날짜 추가 0430

    protected Cparticipant() {
    }

    @Builder
    public Cparticipant(Member member, Croom croom,  boolean isRoomOwner) {
        this.setMember(member);
        this.croom = croom;
        this.setJoinDate(LocalDateTime.now());
        this.setIsRoomOwner(isRoomOwner);
        this.setRoom(croom);
    }

}