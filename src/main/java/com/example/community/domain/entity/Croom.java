package com.example.community.domain.entity;

import com.example.community.dto.CroomDto;
import com.example.domain.Member;
import com.example.domain.Room;
import com.example.community.domain.entity.Cparticipant;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter
@Entity
@Table(name = "croom")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)


public class Croom extends Room {

    @OneToMany(mappedBy = "croom", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Cparticipant> cparticipants;

    //추가 representative대용 0430
    @Column(length = 10, nullable = false)
    private String author;

    @Column(length = 10, nullable = false)
    private String detailCategory;

    @Column(length = 10, nullable = false)
    private String communityCategory;

    @Column
    private String region;

    @Column
    private String detailRegion;

    @Column
    private Double payment;


    //날짜 추가 0430
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;



    @Builder
    public Croom(Integer roomId,String author, Integer recruitNum, Integer currentNum, Integer roomCategory, String roomTitle, String communityCategory, String detailCategory, String region, String detailRegion, Double payment, Member member
                ) {
        this.setRoomId(roomId);
        this.author=author;
        this.setRecruitNum(recruitNum);
        this.currentNum = currentNum != null ? currentNum : 1;
        this.setRoomCategory(roomCategory);
        this.setRoomTitle(roomTitle);
        this.communityCategory = communityCategory;
        this.detailCategory = detailCategory;
        this.region = region;
        this.detailRegion = detailRegion;
        this.payment = payment;
    }


    public void increaseCurrentNum() {
        if (this.getCurrentNum() == null) {
            this.setCurrentNum(1);
        } else {
            this.currentNum += 1;
        }
    }





    public void updateFromDto(CroomDto croomDto) {
        if (croomDto.getRoomTitle() != null) {
            this.setRoomTitle(croomDto.getRoomTitle());
        }
        if (croomDto.getCommunityCategory() != null) {
            this.communityCategory = croomDto.getCommunityCategory();
        }
        if (croomDto.getDetailCategory() != null) {
            this.detailCategory = croomDto.getDetailCategory();
        }
        if (croomDto.getRegion() != null) {
            this.region = croomDto.getRegion();
        }
        if (croomDto.getDetailRegion() != null) {
            this.detailRegion = croomDto.getDetailRegion();
        }
        if (croomDto.getPayment() != null) {
            this.payment = croomDto.getPayment();
        }
    }




}