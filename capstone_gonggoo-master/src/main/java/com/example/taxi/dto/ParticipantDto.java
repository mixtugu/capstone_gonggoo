package com.example.taxi.dto;

import com.example.taxi.domain.entity.TaxiParticipant;
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
    private String memberLoginId;

    public ParticipantDto(String participantName, String memberLoginId) {
        this.participantName = participantName;
        this.memberLoginId = memberLoginId;
    }

    public static ParticipantDto fromEntity(TaxiParticipant taxiParticipant) {
        return new ParticipantDto(
                taxiParticipant.getMember().getName(),
                taxiParticipant.getMember().getLoginId()
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
