package com.example.taxi.dto;

import com.example.domain.Member;
import com.example.taxi.domain.entity.Taxi;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TaxiDto {
    private Integer roomId;
    private String author;
    private String roomTitle;
    private String roomCategory;
    private Integer recruitNum;
    private Integer currentNum;
    private Integer predicttotalprice;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String departure;
    private String destination;
    private Member member;

    public Taxi toEntity() {
        Taxi build = Taxi.builder()
                .roomId(roomId)
                .author(author)
                .roomTitle(roomTitle)
                .roomCategory(3)
                .recruitNum(recruitNum)
                .currentNum(currentNum)
                .predicttotalprice(predicttotalprice)
                .departure(departure)
                .destination(destination)
                .member(member)
                .build();
        return build;
    }

    @Builder
    public TaxiDto(Integer roomId, String author, String roomTitle, String roomCategory, Integer recruitNum, Integer currentNum, Integer predicttotalprice, LocalDateTime createdDate, LocalDateTime modifiedDate, String departure, String destination, Member member) {
        this.roomId = roomId;
        this.author = author;
        this.roomTitle = roomTitle;
        this.roomCategory = roomCategory;
        this.recruitNum = recruitNum;
        this.currentNum = currentNum;
        this.predicttotalprice = predicttotalprice;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.departure = departure;
        this.destination = destination;
        this.member = member;
    }
}