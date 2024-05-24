package com.example.domain;

import com.example.service.MemberService;
import com.example.service.RoomService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.example.service.MemberService;

@Entity
@Table(name = "ChatMessage")
@Getter @Setter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MessageID")
    private Integer messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoomID", referencedColumnName = "RoomID")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SenderID", referencedColumnName = "MEMBER_ID")
    private Member sender;

    @Column(name = "MessageText", columnDefinition = "TEXT")
    private String messageText;

    @Column(name = "Timestamp")
    private LocalDateTime timestamp;

    private String formattedTimestamp;

    public ChatMessage() {
        this.formattedTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }


    public ChatMessage(Room room, Member sender) {
        this.room = room;
        this.sender = sender;
        //this.messageText = messageText;
        this.timestamp = LocalDateTime.now();
        this.formattedTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getFrom() {
        return sender.getName();
    }
    public String getText() {
        return messageText;
    }
    public Member getMember() { return sender; }
    public long getMemberID() { return sender.getId(); }
    public Integer getRoomID() { return room.getRoomId(); }
}
