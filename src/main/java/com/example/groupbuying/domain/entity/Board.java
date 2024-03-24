package com.example.groupbuying.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String author;

    @Column(length = 100, nullable = false)
    private String title;

    @ColumnDefault("1")
    private Integer headCount;

    private Integer totalPrice;

    @ColumnDefault("0")
    private Integer currentPrice;

    private Integer itemPrice;

    private String siteName;

    private String itemName;

    @Column
    private Long fileId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Builder
    public Board(Long id, String author, String title, String itemName, Integer headCount, Integer totalPrice, Integer currentPrice, Integer itemPrice, String siteName, Long fileId) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.itemName = itemName;
        this.headCount = headCount;
        this.totalPrice = totalPrice;
        this.currentPrice = currentPrice;
        this.itemPrice = itemPrice;
        this.siteName = siteName;
        this.fileId = fileId;
    }

    public void increaseHeadCount() {
        if (this.headCount == null) {
            this.headCount = 1;
        } else {
            this.headCount += 1;
        }
    }

    public void addPrice(int amount) {
        if (this.currentPrice == null) {
            this.currentPrice = 0; //
        }
        this.currentPrice += amount;
    }

}
