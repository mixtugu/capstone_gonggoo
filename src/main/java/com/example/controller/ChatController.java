package com.example.controller;

import com.example.chat.service.ChatMessageService;
import com.example.domain.ChatMessage;
import com.example.domain.Member;
import com.example.domain.Room;
import com.example.model.Message;
import com.example.repository.MemberRepository;
import com.example.repository.RoomRepository;
import com.example.service.RoomMemberService;
import com.example.service.RoomService;
import com.example.session.SessionConst;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; // Import if missing

@Controller
public class ChatController {
    private List<ChatMessage> messages = new ArrayList<>();
    @Autowired
    private RoomService roomService; // Ensure RoomService is autowired
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/chat")
    public String showChat(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                           @RequestParam(value = "roomId") Integer roomId, Model model) {

        if (loginMember == null) {
            return "home";
        }

        // Fetch the room object using roomId
        Room room = roomService.findOne(roomId);
        List<ChatMessage> chatMessageList = chatMessageService.findChatMessageByRoomId(roomId);
        model.addAttribute("room", room);
        model.addAttribute("member", loginMember);
        model.addAttribute("ChatMessage", chatMessageList);
        model.addAttribute("message", new Message());
        model.addAttribute("messages", messages);
        return "chat";
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message sendMessageLive(Message message) throws Exception {
        // from과 text는 HTML escape 처리
        // message 객체에 데이터 저장 후 소켓으로 전송
        String escapedText = HtmlUtils.htmlEscape(message.getText());
        String escapedFrom = HtmlUtils.htmlEscape(message.getFrom());
        Integer roomId = message.getRoom_id();
        Long memberId = message.getFrom_id();

        // chatmessage로 bd에 저장
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageText(escapedText);
        chatMessage.setRoom(roomRepository.findOne(roomId));
        chatMessage.setSender(memberRepository.findOne(memberId));
        chatMessageService.saveMessage(chatMessage);

        return new Message(escapedFrom, escapedText);
    }
}
