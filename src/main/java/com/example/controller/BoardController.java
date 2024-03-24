package com.example.controller;


import com.example.domain.Member;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public String post() {
        return "items/post.html";
    }

    @PostMapping("/post")
    public String write(@RequestParam("file") MultipartFile files, BoardDto boardDto) {
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

            Long fileId = fileService.saveFile(fileDto);
            boardDto.setFileId(fileId);
            boardService.savePost(boardDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "items/edit.html";
    }

    @GetMapping("/post/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        List<ParticipantDto> participantDtos = participantService.getParticipantDtosByBoardId(id);

        model.addAttribute("post", boardDto);
        model.addAttribute("participantDtos", participantDtos);
        return "items/detail.html";
    }

    @PutMapping("/post/edit/{id}")
    public String update(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    @DeleteMapping("/post/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deletePost(id);
        return "redirect:/";
    }
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        FileDto fileDto = fileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
                .body(resource);
    }
    /*
    @PostMapping("/participate/{postId}")
    public String participate(@PathVariable Long postId, @RequestParam("quantity") int quantity, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        participantService.addParticipant(postId, usernamen, quantity);
        return "redirect:/post/" + postId;
    }

     */

    @PostMapping("/post/{id}/participate")
    public String participate(@PathVariable Long id, @RequestParam("quantity") int quantity, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            return "redirect:/login";
        }
        participantService.addParticipant(id, loginMember.getLoginId(), quantity);
        boardService.increaseParticipantCount(id);
        return "redirect:/post/" + id;
    }
}
