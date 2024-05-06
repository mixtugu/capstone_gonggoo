package com.example.taxi.domain.entity;

import com.example.domain.Member;
import com.example.domain.Room;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter
@Entity(name = "taxi")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Taxi extends Room {

    @OneToMany(mappedBy = "taxi", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<TaxiParticipant> taxiParticipants;

    @Column(length = 10, nullable = false)
    private String author;

    private Integer predicttotalprice;

    private String destination;

    private String departure;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;



    @Builder
    public Taxi(Integer roomId, String author, String roomTitle, Integer roomCategory, Integer recruitNum, Integer currentNum, Integer predicttotalprice, String departure, String destination, Member member) {
        this.setRoomId(roomId);
        this.author = author;
        this.setRoomTitle(roomTitle);
        this.setRoomCategory(roomCategory);
        this.setRecruitNum(recruitNum);
        this.currentNum = currentNum != null ? currentNum : 1;
        this.predicttotalprice = predicttotalprice;
        this.departure = departure;
        this.destination = destination;
        this.setMember(member);
    }

    public void increaseCurrentNum() {
        if (this.getCurrentNum() == null) {
            this.setCurrentNum(1);
        } else {
            this.currentNum += 1;
        }
    }


//    public void addPrice(int amount) {
//        if (this.currentPrice == null) {
//            this.currentPrice = 0; //
//        }
//        this.currentPrice += amount;
//    }

//    public void subtractPrice(int amount) {
//        this.currentPrice -= amount;
//    }
//
//    public void updatePrice(int newPrice) {
//        this.currentPrice += newPrice;
//    }

}
