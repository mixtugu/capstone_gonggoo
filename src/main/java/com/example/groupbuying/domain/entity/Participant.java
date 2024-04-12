package com.example.groupbuying.domain.entity;

import com.example.domain.Member;
import com.example.domain.RoomMember;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Participant extends RoomMember {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private LocalDateTime joinedAt = LocalDateTime.now();

    private Integer quantity;

    protected Participant() {
    }


    @Builder
    public Participant(Member member, Board board, Integer quantity, boolean isRoomOwner) {
        this.setMember(member);
        this.board = board;
        this.quantity = quantity;
        this.setJoinDate(LocalDateTime.now());
        this.setIsRoomOwner(isRoomOwner);
        this.setRoom(board);
    }


    public void updateQuantity(Integer newQuantity) {
        // 새로운 수량을 현재 엔티티의 수량 필드에 할당합니다.
        this.quantity = newQuantity;
    }
}
