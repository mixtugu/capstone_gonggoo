package com.example.groupbuying.dto;

import com.example.domain.Member;
import com.example.groupbuying.domain.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    private Integer roomId;
    private String author;
    private String roomTitle;
    private String roomCategory;
    private String itemName;
    private Integer recruitNum;
    private Integer currentNum;
    private Integer totalPrice;
    private Integer currentPrice;
    private Integer itemPrice;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String siteName;
    private Integer fileId;
    private Member member;

    public Board toEntity() {
        Board build = Board.builder()
                .roomId(roomId)
                .author(author)
                .roomTitle(roomTitle)
                .roomCategory(2)
                .itemName(itemName)
                .recruitNum(recruitNum)
                .currentNum(currentNum)
                .totalPrice(totalPrice)
                .currentPrice(currentPrice)
                .itemPrice(itemPrice)
                .siteName(siteName)
                .fileId(fileId)
                .member(member)
                .build();
        return build;
    }

    @Builder
    public BoardDto(Integer roomId, String author, String roomTitle, String roomCategory, String itemName, Integer recruitNum, Integer currentNum, Integer totalPrice, Integer currentPrice, Integer itemPrice, LocalDateTime createdDate, LocalDateTime modifiedDate, String siteName, Integer fileId, Member member) {
        this.roomId = roomId;
        this.author = author;
        this.roomTitle = roomTitle;
        this.roomCategory = roomCategory;
        this.itemName = itemName;
        this.recruitNum = recruitNum;
        this.currentNum = currentNum;
        this.totalPrice = totalPrice;
        this.currentPrice = currentPrice;
        this.itemPrice = itemPrice;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.siteName = siteName;
        this.fileId = fileId;
        this.member = member;
    }
}