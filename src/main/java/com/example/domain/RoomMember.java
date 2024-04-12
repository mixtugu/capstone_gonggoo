package com.example.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RoomMember")
@Inheritance(strategy = InheritanceType.JOINED)
public class RoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoomID", referencedColumnName = "RoomID")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "MEMBER_ID")
    private Member member; // Renamed User to Member

    @Column(name = "JoinDate")
    private LocalDateTime joinDate;

    @Column(name = "IsRoomOwner", nullable = false)
    private Boolean isRoomOwner;
}
