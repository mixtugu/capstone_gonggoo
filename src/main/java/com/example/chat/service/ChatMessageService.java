package com.example.chat.service;

import com.example.dto.ChatMessageDTO;
import com.example.domain.Member;
import com.example.domain.Room;
import com.example.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.domain.ChatMessage;
import com.example.chat.repository.ChatMessageRepository;
import com.example.repository.RoomRepository;

import java.time.LocalDateTime;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    private RoomRepository roomRepository;

    @Autowired
    private MemberRepository memberRepository; // Assuming you have this for looking up the member

    public void saveMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }
}
