package com.example.chat.service;

import com.example.domain.RoomMember;
import com.example.dto.ChatMessageDTO;
import com.example.domain.Member;
import com.example.domain.Room;
import com.example.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.domain.ChatMessage;
import com.example.chat.repository.ChatMessageRepository;
import com.example.repository.RoomRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    private RoomRepository roomRepository;

    @Autowired
    private MemberRepository memberRepository; // Assuming you have this for looking up the member

    public void saveMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }

    public List<ChatMessage> findChatMessageByRoomId(Integer roomId) {
        return em.createQuery("SELECT cm FROM ChatMessage cm WHERE cm.room.roomId = :roomId", ChatMessage.class)
                .setParameter("roomId", roomId)
                .getResultList();
    }
}
