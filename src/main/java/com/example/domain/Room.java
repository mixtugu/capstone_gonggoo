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
