package com.example.controller;


import com.example.domain.Member;
import com.example.taxi.dto.TaxiDto;
import com.example.taxi.dto.ParticipantDto;
import com.example.taxi.service.TaxiService;
import com.example.taxi.service.TaxiParticipantService;
import com.example.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TaxiController {

    private TaxiService taxiService;

    private TaxiParticipantService taxiParticipantService;

    public TaxiController(TaxiService taxiService, TaxiParticipantService taxiParticipantService) {
        this.taxiService = taxiService;
        this.taxiParticipantService = taxiParticipantService;
    }

    @GetMapping("/taxi")
    public String list(Model model) {
        List<TaxiDto> taxiDtoList = taxiService.getTaxiList();
        model.addAttribute("postList", taxiDtoList);
        return "taxi/list";
    }

    @GetMapping("/taxi_post")
    public String post(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Member loginMember = session != null ? (Member) session.getAttribute(SessionConst.LOGIN_MEMBER) : null;

        if (loginMember != null) {
            model.addAttribute("currentUserName", loginMember.getName());
        }

        return "taxi/post";
    }

    @PostMapping("/taxi_post")
    public String write(TaxiDto taxiDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Integer taxiId = taxiService.savePost(taxiDto);
        taxiParticipantService.addParticipant(taxiId, loginMember.getLoginId(), true);
        return "redirect:/";
    }


    @GetMapping("/taxi_post/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        TaxiDto taxiDto = taxiService.getPost(id);
        model.addAttribute("taxi_post", taxiDto);
        return "taxi/edit";
    }

    @GetMapping("/taxi_post/{id}")
    public String detail(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Member loginMember = session != null ? (Member) session.getAttribute(SessionConst.LOGIN_MEMBER) : null;

        TaxiDto taxiDto = taxiService.getPost(id);
        List<ParticipantDto> participantDtos = taxiParticipantService.getParticipantDtosByTaxiId(id);
        boolean isParticipated = false;
        Long participantId = null;

        if (loginMember != null) {
            for (ParticipantDto participant : participantDtos) {
                if (participant.getMemberLoginId().equals(loginMember.getLoginId())) {
                    isParticipated = true;
                    participantId = participant.getId();
                    break;
                }
            }
            model.addAttribute("currentUserName", loginMember.getName());
        }

        model.addAttribute("post", taxiDto);
        model.addAttribute("participantDtos", participantDtos);
        model.addAttribute("isParticipated", isParticipated);
        if (participantId != null) {
            model.addAttribute("participantId", participantId);
        }
        return "taxi/detail";
    }

    @PutMapping("/taxi_post/edit/{id}")
    public String update(TaxiDto taxiDto) {
        taxiService.savePost(taxiDto);
        return "redirect:/";
    }

    @DeleteMapping("/taxi_post/{id}")
    public String delete(@PathVariable("id") Integer id) {
        taxiService.deletePost(id);
        return "redirect:/";
    }
//    @GetMapping("/download/{fileId}")
//    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Integer fileId) throws IOException {
//        FileDto fileDto = fileService.getFile(fileId);
//        Path path = Paths.get(fileDto.getFilePath());
//        Resource resource = new InputStreamResource(Files.newInputStream(path));
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
//                .body(resource);
//    }

    @PostMapping("/taxi_post/{id}/participate")
    public String participate(@PathVariable Integer id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            return "redirect:/login";
        }
        System.out.println("0") ;
        taxiParticipantService.addParticipant(id, loginMember.getLoginId(), false);
        System.out.println("1") ;
        taxiService.increaseParticipantCount(id);
        System.out.println("2") ;
        return "redirect:/taxi_post/" + id;
    }

//    @GetMapping("/taxi_post/{id}/withdraw")
//    public String withdrawParticipation(@PathVariable Integer id, HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            return "redirect:/login";
//        }
//        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
//        if (loginMember == null) {
//            return "redirect:/login";
//        }
//        boolean success = participantService.withdrawFromTaxi(id, loginMember.getLoginId());
//        if (success) {
//            taxiService.decreaseParticipantCount(id);
//        } else {
//
//        }
//        return "redirect:/taxi_post/" + id;
//    }

//    @PostMapping("/taxi_post/{taxiId}/editQuantity")
//    public String updateParticipantQuantity(@PathVariable Integer taxiId, @RequestParam("newQuantity") int quantity, HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            return "redirect:/login";
//        }
//        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
//        if (loginMember == null) {
//            return "redirect:/login";
//        }
//
//        return "redirect:/taxi_post/" + taxiId;
//    }

    @GetMapping("/taxiList")
    public String listPosts(@RequestParam(name = "search", required = false) String search, Model model) {
        List<TaxiDto> taxiDtoList;
        if (search != null && !search.trim().isEmpty()) {
            taxiDtoList = taxiService.searchByTitle(search);
        } else {
            taxiDtoList = taxiService.getTaxiList();
        }
        model.addAttribute("postList", taxiDtoList);
        return "taxi/list.html";
    }

}
