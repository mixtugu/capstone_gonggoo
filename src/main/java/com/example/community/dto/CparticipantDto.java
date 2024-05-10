package com.example.community.dto;

import com.example.community.domain.entity.Cparticipant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CparticipantDto {

    private Long id;
    private String cparticipantName;
    private String memberLoginId;

    public CparticipantDto(String cparticipantName, String memberLoginId) {
        this.cparticipantName = cparticipantName;
        this.memberLoginId = memberLoginId;
    }

    public static CparticipantDto fromEntity(Cparticipant cparticipant) {
        return new CparticipantDto(
                cparticipant.getMember().getName(),
                cparticipant.getMember().getLoginId()
        );
    }
}