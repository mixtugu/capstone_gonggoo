package com.example.controller;

import com.example.domain.Member;
import com.example.domain.Room;
import com.example.model.Message;
import com.example.repository.RoomRepository;
import com.example.service.RoomMemberService;
import com.example.service.RoomService;
import com.example.session.SessionConst;
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
    private List<Message> messages = new ArrayList<>();

    @Autowired
    private RoomService roomService; // Ensure RoomService is autowired

    @GetMapping("/chat")
    public String showChat(@RequestParam("roomId") Integer roomId, Model model) {
        // Fetch the room object using roomId
        Room room = roomService.findOne(roomId); // Assume this method exists in your RoomService
        model.addAttribute("room", room);
        model.addAttribute("message", new Message());
        model.addAttribute("messages", messages);
        return "chat";
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message sendMessageLive(Message message) throws Exception {
        // from과 text는 HTML escape 처리
        String escapedFrom = HtmlUtils.htmlEscape(message.getFrom());
        String escapedText = HtmlUtils.htmlEscape(message.getText());

        return new Message(escapedFrom, escapedText);
    }
}
