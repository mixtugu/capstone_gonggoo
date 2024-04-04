package com.example.controller;

import com.example.community.*;
import com.example.domain.Member;
import com.example.session.SessionConst;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

@Controller//
public class CommunityController {
    private final CommunityGathering group;
    private final ManagingParticipants managing;

    public CommunityController(CommunityGathering group, ManagingParticipants managing) {
        this.group = group;
        this.managing = managing;
    }

    @GetMapping("/communityIndex")
    public String accountIndex(Model model) {
        model.addAttribute("group", group);
        return "community/communityIndex"; // Updated path
    }

    @GetMapping("/createRoom")
    public String createRoom(Model model) {
        model.addAttribute("room", new Room());
        return "community/createRoom"; // Updated path
    }

    @PostMapping("/createRoom")
    public String createRoomSubmit(@ModelAttribute Room roomForm, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "community/createRoom";
        }

        boolean roomExists = group.getRooms().stream()
                .anyMatch(existingRoom -> existingRoom.getName().equals(roomForm.getName()));
        if (roomExists) {
            model.addAttribute("room", new Room());
            model.addAttribute("nameError", "동일한 이름의 방이 이미 존재합니다.");
            return "community/createRoom";
        }

        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        //추가
        String customCategory = request.getParameter("customCategory");
        if (!List.of("스터디","운동","보드게임","취미모임").contains(roomForm.getCategory())) {
            customCategory = null; // 세부 지역이 필요 없는 지역인 경우 null로 설정
        }

        // 세부 지역 정보 처리
        String customRegion = request.getParameter("customRegion");
        if (!List.of("충청남도", "충청북도", "강원도", "전라남도", "전라북도", "경상북도", "경상남도").contains(roomForm.getRegion())) {
            customRegion = null; // 세부 지역이 필요 없는 지역인 경우 null로 설정
        }



        // 방 생성에 필요한 Representative 객체 생성
        Representative representative = new Representative();
        representative.setName(loginMember.getName());

        // Room 객체를 모든 필드를 포함하는 생성자를 사용하여 생성 ///추가
        Room newRoom = new Room(roomForm.getName(), roomForm.getCategory(), customCategory, roomForm.getRegion(),
                customRegion, roomForm.getNumberOfParticipants(), roomForm.getPayment(), representative);

        group.createNewSharingRoom(newRoom, representative);

        return "redirect:/";
    }



    @GetMapping("/joinRoom/search")
    public String searchRooms(@RequestParam(name = "searchName", required = false) String searchName,
                              @RequestParam(name = "searchCategory", required = false) String searchCategory,//추가
                              @RequestParam(name = "searchRegion", required = false) String searchRegion, Model model) {
        List<Room> filteredRooms = group.getRooms().stream()
                .filter(room -> (searchName == null || searchName.isEmpty() || room.getName().contains(searchName)) &&
                        (searchCategory == null || searchCategory.isEmpty() || room.getCategory().equalsIgnoreCase(searchCategory) ||
                                (room.getDetailCategory() != null && room.getDetailCategory().equalsIgnoreCase(searchCategory)))//추가
                        &&(searchRegion == null || searchRegion.isEmpty() || room.getRegion().equalsIgnoreCase(searchRegion) ||
                                (room.getDetailRegion() != null && room.getDetailRegion().equalsIgnoreCase(searchRegion))))
                .collect(Collectors.toList());

        model.addAttribute("rooms", filteredRooms);
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("searchRegion", searchRegion);
        return "community/roomsByCategory";
    }

    @GetMapping("/joinRoom")
    public String joinRoom(@RequestParam(name = "selectedRoom", required = false) String selectedName,
                           @RequestParam(name = "Category", required = false) String Category, Model model) {
        List<Room> rooms;
        if (Category != null && !Category.isEmpty()) {
            rooms = group.getRooms().stream()
                    .filter(room -> room.getCategory().equals(Category))
                    .collect(Collectors.toList());
        } else {
            rooms = group.getRooms();
        }
        model.addAttribute("rooms", rooms);
        return "community/joinRoom"; // Updated path
    }


    @PostMapping("/joinRoom")
    public String joinRoomSubmit(@RequestParam("name") String name, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            return "redirect:/login";
        }

        // 방 이름으로 방을 찾음
        Room selectedRoom = group.getRooms().stream()
                .filter(room -> room.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (selectedRoom == null) {
            model.addAttribute("error", "방을 찾을 수 없습니다");
            return "redirect:/joinRoom";
        }

        // 이미 참여자가 존재하는지 확인
        if (selectedRoom.isParticipantExists(loginMember.getName())) {
            model.addAttribute("error", "이미 참여한 방입니다");
            return "redirect:/roomDetails?name=" + name;
        }

        // 참여자를 추가
        Participant participant = new Participant();
        participant.setName(loginMember.getName());
        selectedRoom.addParticipant(participant);

        return "redirect:/roomDetails?name=" + name;
    }

   @GetMapping("/roomsByCategory")
   public String roomsByCategory(@RequestParam("category") String category, Model model) {
       List<String> categorysWithDetail = List.of("스터디","운동","보드게임","취미모임");

       List<Room> filteredRooms = group.getRooms().stream()
               .filter(room -> {
                   if (categorysWithDetail.contains(room.getRegion()) && room.getDetailCategory() != null && !room.getDetailCategory().isEmpty()) {
                       return room.getDetailCategory().equalsIgnoreCase(category);
                   } else {
                       return room.getCategory().equalsIgnoreCase(category);
                   }
               })
               .collect(Collectors.toList());

       model.addAttribute("rooms", filteredRooms);
       model.addAttribute("region", category);
       return "community/roomsByCategory";
   }



    @GetMapping("/roomsByRegion")
    public String roomsByRegion(@RequestParam("region") String region, Model model) {
        List<String> regionsWithDetail = List.of("충청남도", "충청북도", "강원도", "전라남도", "전라북도", "경상북도", "경상남도");

        List<Room> filteredRooms = group.getRooms().stream()
                .filter(room -> {
                    if (regionsWithDetail.contains(room.getRegion()) && room.getDetailRegion() != null && !room.getDetailRegion().isEmpty()) {
                        return room.getDetailRegion().equalsIgnoreCase(region);
                    } else {
                        return room.getRegion().equalsIgnoreCase(region);
                    }
                })
                .collect(Collectors.toList());

        model.addAttribute("rooms", filteredRooms);
        model.addAttribute("region", region);
        return "community/roomsByRegion";
    }


    @PostMapping("/leaveRoom")
    public String leaveRoomSubmit(@RequestParam("name") String name, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Room selectedRoom = group.getRooms().stream()
                .filter(room -> room.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (selectedRoom != null && loginMember != null) {
            boolean success = selectedRoom.removeParticipant(loginMember.getName());
            if (!success) {
                model.addAttribute("error", "Error leaving the room.");
                return "redirect:/roomDetails?name=" + name;
            }
        } else {
            model.addAttribute("error", "The room could not be found or user is not valid.");
            return "redirect:/joinRoom";
        }

        return "redirect:/communityIndex";
    }

    @GetMapping("/roomDetails")
    public String roomDetails(@RequestParam("name") String name, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Room selectedRoom = group.getRooms().stream()
                .filter(room -> room.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (selectedRoom != null) {
            boolean isParticipant = selectedRoom.isParticipantExists(loginMember.getName());
            model.addAttribute("room", selectedRoom);
            model.addAttribute("isParticipant", isParticipant);
            return "community/roomDetails";
        } else {
            model.addAttribute("error", "The specified room could not be found.");
            return "redirect:/";
        }
    }



    /*@GetMapping("/manageRooms")
    public String manageRooms(Model model) {
        model.addAttribute("rooms", group.getRooms());
        return "community/manageRooms"; // Updated path
    }

    @PostMapping("/manageRooms")
    public String manageRoomsSubmit(@ModelAttribute Room selectedRoom) {
        managing.manageParticipantRooms(selectedRoom);
        return "redirect:/";
    }*/





    @GetMapping("/goBack")
    public String goBack(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/";
        }
    }
}