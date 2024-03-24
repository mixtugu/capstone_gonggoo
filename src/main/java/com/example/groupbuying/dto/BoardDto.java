package com.example.groupbuying.dto;

import com.example.groupbuying.domain.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private String author;
    private String title;
    private String itemName;
    private Integer headCount;
    private Integer totalPrice;
    private Integer currentPrice;
    private Integer itemPrice;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String siteName;
    private Long fileId;

    public Board toEntity() {
        Board build = Board.builder()
                .id(id)
                .author(author)
                .title(title)
                .itemName(itemName)
                .headCount(headCount)
                .totalPrice(totalPrice)
                .currentPrice(currentPrice)
                .itemPrice(itemPrice)
                .siteName(siteName)
                .fileId(fileId)
                .build();
        return build;
    }

    @Builder
    public BoardDto(Long id, String author, String title, String itemName, Integer headCount, Integer totalPrice, Integer currentPrice, Integer itemPrice, LocalDateTime createdDate, LocalDateTime modifiedDate, String siteName, Long fileId) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.itemName = itemName;
        this.headCount = headCount;
        this.totalPrice = totalPrice;
        this.currentPrice = currentPrice;
        this.itemPrice = itemPrice;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.siteName = siteName;
        this.fileId = fileId;
    }
}