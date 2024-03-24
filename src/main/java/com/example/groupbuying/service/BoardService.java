package com.example.groupbuying.service;

import com.example.groupbuying.domain.entity.Board;
import com.example.groupbuying.domain.repository.BoardRepository;
import com.example.groupbuying.dto.BoardDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }
    @Transactional
    public List<BoardDto> getBoardList() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for(Board board : boardList) {
            BoardDto boardDto = BoardDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .author(board.getAuthor())
                    .itemName(board.getItemName())
                    .headCount(board.getHeadCount())
                    .currentPrice(board.getCurrentPrice())
                    .totalPrice(board.getTotalPrice())
                    .itemPrice(board.getItemPrice())
                    .createdDate(board.getCreatedDate())
                    .siteName(board.getSiteName())

                    .build();
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    public BoardDto getPost(Long id) {
        Board board = boardRepository.findById(id).get();

        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .author(board.getAuthor())
                .itemName(board.getItemName())
                .headCount(board.getHeadCount())
                .currentPrice(board.getCurrentPrice())
                .totalPrice(board.getTotalPrice())
                .createdDate(board.getCreatedDate())
                .itemPrice(board.getItemPrice())
                .siteName(board.getSiteName())
                .fileId(board.getFileId())
                .build();
        return boardDto;
    }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }

    public void increaseParticipantCount(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        board.increaseHeadCount();
        boardRepository.save(board);
    }
}