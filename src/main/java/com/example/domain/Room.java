package com.example.domain; // Adjust the package name to match your project structure

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@Entity
@Table(name = "Room")
@Inheritance(strategy = InheritanceType.JOINED)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomID")
    private Integer roomId;

    @Column(name = "RoomTitle", nullable = false, length = 255)
    private String roomTitle;

    @Column(name = "RoomCategory")
    private Integer roomCategory;

    @Column(name = "RecruitNum")
    private Integer recruitNum;


    @Column(name = "CurrentNum", nullable = false)
    protected Integer currentNum;

    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member member;
}




// INSERT INTO room (current_num, recruit_num, room_category, room_title, member_id) VALUES
//(1, 4, 1, '서울에서 부산까지의 카풀', 1) DB 예제
//INSERT INTO room (current_num, recruit_num, room_category, room_title) VALUES
//(1, 4, 1, '서울에서 부산까지의 카풀'),
//(1, 4, 2, '해외직구 공동구매');
