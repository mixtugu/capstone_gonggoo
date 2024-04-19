package com.example.taxi.domain.entity;

import com.example.domain.Member;
import com.example.domain.RoomMember;
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
public class TaxiParticipant extends RoomMember {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxi_id")
    private Taxi taxi;

    private LocalDateTime joinedAt = LocalDateTime.now();

    protected TaxiParticipant() {
    }


    @Builder
    public TaxiParticipant(Member member, Taxi taxi, boolean isRoomOwner) {
        this.setMember(member);
        this.taxi = taxi;
        this.setJoinDate(LocalDateTime.now());
        this.setIsRoomOwner(isRoomOwner);
        this.setRoom(taxi);
    }
}
