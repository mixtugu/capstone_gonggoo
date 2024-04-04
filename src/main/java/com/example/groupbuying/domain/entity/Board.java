package com.example.groupbuying.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter
@Entity(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Participant> participants;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String author;

    @Column(length = 100, nullable = false)
    private String title;

    private Integer headCount = 1;

    private Integer totalPrice;

    private Integer currentPrice = 0;

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
        this.headCount = headCount != null ? headCount : 1;
        this.totalPrice = totalPrice;
        this.currentPrice = currentPrice != null ? currentPrice : 0;
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

    public void subtractPrice(int amount) {
        this.currentPrice -= amount;
    }

    public void updatePrice(int newPrice) {
        this.currentPrice += newPrice;
    }

}
