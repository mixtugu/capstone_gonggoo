package com.example.groupbuying.dto;

import com.example.domain.Member;
import com.example.groupbuying.domain.entity.Board;
import com.example.groupbuying.domain.entity.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ParticipantDto {

    private Long id;
    private String participantName;
    private Integer quantity;
    private String memberLoginId;

    public ParticipantDto(String participantName, Integer quantity, String memberLoginId) {
        this.participantName = participantName;
        this.quantity = quantity;
        this.memberLoginId = memberLoginId;
    }

    public static ParticipantDto fromEntity(Participant participant) {
        return new ParticipantDto(
                participant.getMember().getName(),
                participant.getQuantity(),
                participant.getMember().getLoginId()
        );
    }
    /*
    private Long id;

    private Member member;

    private Board board;

    protected ParticipantDto() {
    }

    public Participant toEntity() {
        Participant build = Participant.builder()
                .member(member)
                .board(board)
                .build();
        return build;
    }

    public ParticipantDto(Member member, Board board) {
        this.member = member;
        this.board = board;
    }
     */
}
