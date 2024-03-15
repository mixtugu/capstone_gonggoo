package com.example.controller;

import com.example.model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {
    private List<Message> messages = new ArrayList<>();

    @GetMapping("/chat")
    public String showChat(Model model) {
        model.addAttribute("message", new Message());
        model.addAttribute("messages", messages);
        return "chat";
    }

//    @PostMapping("/send")
//    public String sendMessage(@ModelAttribute Message message) {
//        messages.add(message);
//        return "redirect:/";
//    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message sendMessageLive(Message message) throws Exception {
        // from과 text는 HTML escape 처리
        String escapedFrom = HtmlUtils.htmlEscape(message.getFrom());
        String escapedText = HtmlUtils.htmlEscape(message.getText());

        return new Message(escapedFrom, escapedText);
    }
}