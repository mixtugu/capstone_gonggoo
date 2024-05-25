package com.example.controller;


import com.example.domain.Member;
import com.example.groupbuying.domain.entity.Board;
import com.example.groupbuying.dto.BoardDto;
import com.example.groupbuying.dto.FileDto;
import com.example.groupbuying.dto.ParticipantDto;
import com.example.groupbuying.service.BoardService;
import com.example.groupbuying.service.FileService;
import com.example.groupbuying.service.ParticipantService;
import com.example.groupbuying.util.MD5Generator;
import com.example.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Controller
public class BoardController {

    private BoardService boardService;

    private FileService fileService;

    private ParticipantService participantService;

    public BoardController(BoardService boardService, FileService fileService, ParticipantService participantService) {
        this.boardService = boardService;
        this.fileService = fileService;
        this.participantService = participantService;
    }

    @GetMapping("/group")
    public String list(Model model) {
        List<BoardDto> boardDtoList = boardService.getBoardList();
        model.addAttribute("postList", boardDtoList);
        return "items/list.html";
    }

    @GetMapping("/post")
    public String post(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Member loginMember = session != null ? (Member) session.getAttribute(SessionConst.LOGIN_MEMBER) : null;

        if (loginMember != null) {
            model.addAttribute("currentUserName", loginMember.getName());
        }

        return "items/post.html";
    }

    @PostMapping("/post")
    public String write(@RequestParam("file") MultipartFile files, BoardDto boardDto, HttpServletRequest request) {
        Integer boardId = null;
        try {
            String origFilename = files.getOriginalFilename();
            String filename = new MD5Generator(origFilename).toString();
            String savePath = System.getProperty("user.dir") + "\\files";
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "\\" + filename;
            files.transferTo(new File(filePath));

            FileDto fileDto = new FileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);

            Integer fileId = fileService.saveFile(fileDto);
            boardDto.setFileId(fileId);
            boardId =  boardService.savePost(boardDto);
        } catch(Exception e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            participantService.addParticipant(boardId, loginMember.getLoginId(), 1,true);
        }
        return "redirect:/";
    }

    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "items/edit.html";
    }

    @GetMapping("/post/{id}")
    public String detail(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Member loginMember = session != null ? (Member) session.getAttribute(SessionConst.LOGIN_MEMBER) : null;

        BoardDto boardDto = boardService.getPost(id);
        List<ParticipantDto> participantDtos = participantService.getParticipantDtosByBoardId(id);
        boolean isParticipated = false;
        Long participantId = null;
        Integer participantQuantity = 0;

        if (loginMember != null) {
            for (ParticipantDto participant : participantDtos) {
                if (participant.getMemberLoginId().equals(loginMember.getLoginId())) {
                    isParticipated = true;
                    participantId = participant.getId();
                    participantQuantity = participant.getQuantity();
                    break;
                }
            }
            model.addAttribute("currentUserName", loginMember.getName());
        }
        model.addAttribute("post", boardDto);
        model.addAttribute("participantDtos", participantDtos);
        model.addAttribute("isParticipated", isParticipated);
        model.addAttribute("participantQuantity", participantQuantity);
        if (participantId != null) {
            model.addAttribute("participantId", participantId);
        }
        return "items/detail.html";
    }

    @PutMapping("/post/edit/{roomId}")
    public String updatePost(@PathVariable Integer roomId, BoardDto boardDto) {
        System.out.println(boardDto);
        boardService.updateBoard(roomId, boardDto);
        return "redirect:/";
    }


    @DeleteMapping("/post/{id}")
    public String delete(@PathVariable("id") Integer id) {
        boardService.deletePost(id);
        return "redirect:/";
    }
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Integer fileId) throws IOException {
        FileDto fileDto = fileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/post/{id}/participate")
    public String participate(@PathVariable Integer id, @RequestParam("quantity") int quantity, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            return "redirect:/login";
        }
        participantService.addParticipant(id, loginMember.getLoginId(), quantity,false);
        boardService.increaseParticipantCount(id);
        return "redirect:/post/" + id;
    }

    @GetMapping("/post/{id}/withdraw")
    public String withdrawParticipation(@PathVariable Integer id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            return "redirect:/login";
        }
        boolean success = participantService.withdrawFromBoard(id, loginMember.getLoginId());
        if (success) {
            boardService.decreaseParticipantCount(id);
        } else {

        }
        return "redirect:/post/" + id;
    }

    @PostMapping("/post/{boardId}/editQuantity")
    public String updateParticipantQuantity(@PathVariable Integer boardId, @RequestParam("newQuantity") int quantity, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            return "redirect:/login";
        }

        participantService.updateQuantity(boardId, loginMember.getLoginId(), quantity);

        return "redirect:/post/" + boardId;
    }

    @GetMapping("/boardList")
    public String listPosts(@RequestParam(name = "search", required = false) String search, Model model) {
        List<BoardDto> boardDtoList;
        if (search != null && !search.trim().isEmpty()) {
            boardDtoList = boardService.searchByTitle(search);
        } else {
            boardDtoList = boardService.getBoardList();
        }
        model.addAttribute("postList", boardDtoList);
        return "items/list.html";
    }

}
