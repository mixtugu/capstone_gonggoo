package com.example.controller;
import com.example.domain.Member;
import com.example.community.dto.CroomDto;
import com.example.community.dto.CparticipantDto;
import com.example.community.service.CroomService;
import com.example.community.service.CparticipantService;
import com.example.groupbuying.dto.BoardDto;
import com.example.groupbuying.dto.FileDto;
import com.example.groupbuying.dto.ParticipantDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class CommunityController {
    @Autowired
    private CroomService croomService;
    @Autowired
    private CparticipantService cparticipantService;

    // 방 생성 페이지 요청
    /*@GetMapping("/createRoom")
    public String createRoomPage(Model model) {
        model.addAttribute("room", new CroomDto());
        return "community/createRoom";
    }*/

    @GetMapping("/createRoom")
    public String createRoomPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Member loginMember = session != null ? (Member) session.getAttribute(SessionConst.LOGIN_MEMBER) : null;
        if (loginMember != null) {
            model.addAttribute("currentUserName", loginMember.getName());
        }
        return "community/createRoom";
    }


    // 방 생성 제출 처리
    /*@PostMapping("/createRoom")
    public String createRoom( CroomDto croomDto, HttpServletRequest request) {
        Integer croomId = null;
        try {
        croomId =  croomService.savePost(croomDto);
        } catch(Exception e) {e.printStackTrace();}
        HttpSession session = request.getSession(false);
        if (session != null) {Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        cparticipantService.addParticipant(croomId, loginMember.getLoginId(), true);}
        return "redirect:/roomDetails";
    }*/
    /*@PostMapping("/createRoom")
    public String createRoom(CroomDto croomDto, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            if (loginMember != null) {
                croomDto.setAuthor(loginMember.getName());  // Set author name from the logged-in user
                Integer croomId = croomService.savePost(croomDto);
                cparticipantService.addParticipant(croomId, loginMember.getLoginId(), true);
                return "redirect:/roomDetails?roomId=" + croomId;  // Redirect to room details with room ID
            }
        }
        return "redirect:/login";  // Redirect to login if no user is logged in
    }*///ㅇㄹㅇㄹㅇ러ㅏ임아ㅣ러;
    @PostMapping("/createRoom")
    public String createRoom(CroomDto croomDto, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            if (loginMember != null) {
                croomDto.setAuthor(loginMember.getName());  // Set author name from the logged-in user
                Integer croomId = croomService.savePost(croomDto);
                cparticipantService.addParticipant(croomId, loginMember.getLoginId(), true);
                return "redirect:/roomDetails?roomId=" + croomId;  // Redirect to room details with room ID
            }
        }
        return "redirect:/login";  // Redirect to login if no user is logged in
    }


    @GetMapping("/createRoom/{id}")
    public String detail(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Member loginMember = session != null ? (Member) session.getAttribute(SessionConst.LOGIN_MEMBER) : null;

        CroomDto croomDto = croomService.getPost(id);
        List<CparticipantDto> cparticipantDtos = cparticipantService.getCparticipantDtosByCroomId(id);
        boolean isParticipated = false;
        Long cparticipantId = null;

        if (loginMember != null) {
            for (CparticipantDto cparticipant : cparticipantDtos) {
                if (cparticipant.getMemberLoginId().equals(loginMember.getLoginId())) {
                    isParticipated = true;
                    cparticipantId = cparticipant.getId();
                    break;
                }
            }
            model.addAttribute("currentUserName", loginMember.getName());
        }

        model.addAttribute("createRoom", croomDto);
        model.addAttribute("cparticipantDtos", cparticipantDtos);
        model.addAttribute("isParticipated", isParticipated);
        if (cparticipantId != null) {
            model.addAttribute("participantId", cparticipantId);
        }
        return "community/roomDetails.html";
    }

    @PutMapping("createRoom/edit/{id}")
    public String update(CroomDto croomDto) {
        croomService.savePost(croomDto);
        return "redirect:/";
    }

    @DeleteMapping("createRoom/{id}")
    public String delete(@PathVariable("id") Integer id) {
        croomService.deletePost(id);
        return "redirect:/";
    }

    @PostMapping("createRoom/{id}/cparticipant")
    public String participate(@PathVariable Integer id,  HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            return "redirect:/login";
        }
        cparticipantService.addParticipant(id, loginMember.getLoginId(), false);
        croomService.increaseCparticipantCount(id);
        return "redirect:/createRoom/" + id;
    }

    // 방 검색
    @GetMapping("/joinRoom/search")
    public String searchRooms(@RequestParam(required = false) String roomTitle,
                              @RequestParam(required = false) String communityCategory,
                              @RequestParam(required = false) String region, Model model) {
        model.addAttribute("rooms", croomService.searchRooms(roomTitle, communityCategory, region));
        return "community/roomsByCategory";
    }

    // roomsByCategory의 검색 로직을 위한 컨트롤러 메소드 수정
    @GetMapping("/roomsByCategory/search")
    public String searchRoomsByCategory(@RequestParam(required = false) String roomTitle,
                                        @RequestParam(required = false) String detailCategory,
                                        @RequestParam(required = false) String detailRegion, Model model) {
        model.addAttribute("rooms", croomService.searchRoomsByDetail(roomTitle, detailCategory, detailRegion));
        return "community/roomsByCategory";
    }




    // 방 참여 페이지 요청
    @GetMapping("/joinRoom")
    public String joinRoomPage() {
        return "community/joinRoom";
    }

    // 방 참여 제출 처리
    @PostMapping("/joinRoom")
    public String joinRoom(@RequestParam Integer roomId, @RequestParam String loginId, Model model) {
        if (cparticipantService.isUserParticipated(roomId, loginId)) {
            model.addAttribute("error", "이미 참여중인 방입니다.");
            return "community/joinRoom";
        }
        Integer participantId = cparticipantService.addParticipant(roomId, loginId, false);
        return "redirect:/roomDetails?roomId=" + roomId;
    }

    // 카테고리별 방 조회
    @GetMapping("/roomsByCategory")
    public String roomsByCategory(@RequestParam String communityCategory, Model model) {
        model.addAttribute("rooms", croomService.findByCommunityCategory(communityCategory));
        return "community/roomsByCategory";
    }

    // 지역별 방 조회
    /*@GetMapping("/roomsByRegion")
    public String roomsByRegion(@RequestParam String region, Model model) {
        model.addAttribute("rooms", croomService.findByRegion(region));
        return "community/roomsByRegion";
    }*/

    // 방 나가기 처리
    @PostMapping("/leaveRoom")
    public String leaveRoom(@RequestParam Integer roomId, @RequestParam String loginId) {
        boolean success = cparticipantService.withdrawFromCroom(roomId, loginId);
        if (success) {
            return "redirect:/";
        } else {
            return "redirect:/roomDetails?roomId=" + roomId + "&error=true";
        }
    }

    @GetMapping("/roomDetails")
    public String roomDetails(@RequestParam("roomId") Integer roomId, HttpServletRequest request, Model model) {
        CroomDto roomDetails = croomService.getRoomDetails(roomId);
        List<CparticipantDto> cparticipants = cparticipantService.getCparticipantDtosByCroomId(roomId);
        HttpSession session = request.getSession(false);
        Member loginMember = (session != null) ? (Member) session.getAttribute(SessionConst.LOGIN_MEMBER) : null;
        boolean isParticipated = (loginMember != null) && cparticipantService.isUserParticipated(roomId, loginMember.getLoginId());

        model.addAttribute("room", roomDetails);
        model.addAttribute("cparticipants", cparticipants);
        model.addAttribute("isParticipated", isParticipated);
        model.addAttribute("loginMember", loginMember);  // 로그인한 사용자 정보를 모델에 추가

        return "community/roomDetails";
    }

    // 이전 페이지로 돌아가기
    @GetMapping("/goBack")
    public String goBack(@RequestHeader(value = "Referer", required = false) String referer) {
        return "redirect:" + (referer != null ? referer : "/");
    }
}