package com.example.community.dto;

import com.example.community.domain.entity.Croom;
import com.example.domain.Member;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class CroomDto {
    private Integer roomId;
    private String author;
    private Integer recruitNum;
    private Integer currentNum;
    private Integer roomCategory;
    private String roomTitle;
    private String communityCategory;
    private String detailCategory;
    private String region;
    private String detailRegion;
    private Double payment;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Member member;



    public Croom toEntity() {
        Croom build= Croom.builder()
                .roomId(roomId)
                .author(author)
                .recruitNum(recruitNum)
                .currentNum(currentNum)
                .roomCategory(1)
                .roomTitle(roomTitle)
                .communityCategory(communityCategory)
                .detailCategory(detailCategory)
                .region(region)
                .detailRegion(detailRegion)
                .payment(payment)

                .member(member)
                .build();
        return build;
    }
    @Builder
    public CroomDto(Integer roomId, String author, Integer recruitNum, Integer currentNum, Integer roomCategory,
                    String roomTitle, String communityCategory,String detailCategory, String region, String detailRegion, Double payment,  LocalDateTime createdDate, LocalDateTime modifiedDate, Member member) {
        this.roomId = roomId;
        this.author=author;
        this.recruitNum = recruitNum;
        this.currentNum =currentNum;
        this.roomCategory = roomCategory;
        this.roomTitle =roomTitle;
        this.communityCategory =communityCategory;
        this.detailCategory = detailCategory;
        this.region = region;
        this.detailRegion = detailRegion;
        this.payment = payment;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;

        this.member = member ;
    }

}