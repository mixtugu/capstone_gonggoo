package com.example.groupbuying.domain.entity;

import com.example.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private LocalDateTime joinedAt = LocalDateTime.now();

    private Integer quantity;

    protected Participant() {
    }

    @Builder
    public Participant(Member member, Board board, Integer quantity) {
        this.member = member;
        this.board = board;
        this.quantity = quantity;
        this.joinedAt = LocalDateTime.now();
    }

    public void updateQuantity(Integer newQuantity) {
        // 새로운 수량을 현재 엔티티의 수량 필드에 할당합니다.
        this.quantity = newQuantity;
    }
}
