package com.example.groupbuying.service;

import com.example.groupbuying.domain.entity.Board;
import com.example.groupbuying.domain.entity.Participant;
import com.example.groupbuying.domain.repository.BoardRepository;
import com.example.groupbuying.domain.repository.ParticipantRepository;
import com.example.groupbuying.dto.BoardDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    @Autowired
    private ParticipantRepository participantRepository;

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

    public void decreaseParticipantCount(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id: " + id));
        board.setHeadCount(board.getHeadCount() - 1);
        boardRepository.save(board);
    }

    public boolean withdrawFromBoard(Long boardId, String loginId) {

        Optional<Participant> participantOpt = participantRepository.findByBoardIdAndMember_LoginId(boardId, loginId);

        if (participantOpt.isEmpty()) {
            return false;
        }

        Participant participant = participantOpt.get();
        Board board = participant.getBoard();

        board.setCurrentPrice(board.getCurrentPrice() - (participant.getQuantity() * board.getItemPrice()));
        boardRepository.save(board);
        participantRepository.delete(participant);

        return true;
    }

    public List<BoardDto> searchByTitle(String title) {
        List<Board> boards = boardRepository.findByTitleContaining(title);
        return boards.stream().map(board -> BoardDto.builder()
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
                .fileId(board.getFileId())
                .build()).collect(Collectors.toList());
    }
}