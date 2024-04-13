package com.example.groupbuying.domain.entity;

import com.example.domain.Member;
import com.example.domain.Room;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
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
public class Board extends Room {

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Participant> participants;

    @Column(length = 10, nullable = false)
    private String author;

    private Integer totalPrice;

    private Integer currentPrice = 0;

    private Integer itemPrice;

    private String siteName;

    private String itemName;

    @Column
    private Integer fileId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;



    @Builder
    public Board(Integer roomId, String author, String roomTitle, Integer roomCategory, String itemName, Integer recruitNum, Integer currentNum, Integer totalPrice, Integer currentPrice, Integer itemPrice, String siteName, Integer fileId, Member member) {
        this.setRoomId(roomId);
        this.author = author;
        this.setRoomTitle(roomTitle);
        this.setRoomCategory(roomCategory);
        this.itemName = itemName;
        this.setRecruitNum(recruitNum);
        this.currentNum = currentNum != null ? currentNum : 1;
        this.totalPrice = totalPrice;
        this.currentPrice = currentPrice != null ? currentPrice : 0;
        this.itemPrice = itemPrice;
        this.siteName = siteName;
        this.fileId = fileId;
        this.setMember(member);
    }

    public void increaseCurrentNum() {
        if (this.getCurrentNum() == null) {
            this.setCurrentNum(1);
        } else {
            this.currentNum += 1;
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
