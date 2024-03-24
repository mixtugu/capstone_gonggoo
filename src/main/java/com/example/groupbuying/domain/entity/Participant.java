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
    // Getter 및 필요한 Setter 추가
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

    // 기본 생성자
    protected Participant() {
    }

    // 생성자
    @Builder
    public Participant(Member member, Board board, Integer quantity) {
        this.member = member;
        this.board = board;
        this.quantity = quantity;
        this.joinedAt = LocalDateTime.now();
    }


}
