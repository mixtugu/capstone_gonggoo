package com.example.controller;

import com.example.domain.Member;
import com.example.domain.Room;
import com.example.service.RoomMemberService;
import com.example.session.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
public class ChatListController {
    private final RoomMemberService roomMemberService;

    public ChatListController(RoomMemberService roomMemberService) {
        this.roomMemberService = roomMemberService;
    }

    @GetMapping("/chatList")
    public String homeLogin(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        if (loginMember == null) {
            return "home";
        }

//        String memberIdAsString = String.valueOf(loginMember.getId());
//        System.out.println(memberIdAsString);
        List<Room> room = roomMemberService.findRoomByMemberId(loginMember.getId());
        model.addAttribute("room", room);
        model.addAttribute("member", loginMember);
        return "chatList";
    }
}
