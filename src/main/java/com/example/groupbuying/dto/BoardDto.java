package com.example.groupbuying.dto;

import com.example.domain.Member;
import com.example.groupbuying.domain.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private MyPageDto myPageDto;

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
}
