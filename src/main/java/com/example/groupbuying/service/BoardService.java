package com.example.groupbuying.service;

import com.example.domain.Member;
import com.example.domain.RoomMember;
import com.example.groupbuying.domain.entity.Board;
import com.example.groupbuying.domain.entity.Participant;
import com.example.groupbuying.domain.repository.BoardRepository;
import com.example.groupbuying.domain.repository.ParticipantRepository;
import com.example.groupbuying.dto.BoardDto;
import com.example.groupbuying.dto.MyPageDto;
import com.example.mypage.domain.MyPage;
import com.example.mypage.repository.MyPageRepository;
import com.example.repository.RoomMemberRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private BoardRepository boardRepository;
    private RoomMemberRepository roomMemberRepository;
    private MyPageRepository myPageRepository;
    private ParticipantRepository participantRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, RoomMemberRepository roomMemberRepository, MyPageRepository myPageRepository, ParticipantRepository participantRepository) {
        this.boardRepository = boardRepository;
        this.roomMemberRepository = roomMemberRepository;
        this.myPageRepository = myPageRepository;
        this.participantRepository = participantRepository;
    }

    @Transactional
    public Integer savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getRoomId();
    }
    @Transactional
    public void updateBoard(Integer id, BoardDto boardDto) {
        Board post = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id: " + id));

        post.setAuthor(boardDto.getAuthor());
        post.setItemName(boardDto.getItemName());
        post.setSiteName(boardDto.getSiteName());
        post.setItemPrice(boardDto.getItemPrice());
        post.setTotalPrice(boardDto.getTotalPrice());
        post.setCreatedDate(boardDto.getCreatedDate());
        post.setModifiedDate(boardDto.getModifiedDate());
        post.setRecruitNum(boardDto.getRecruitNum());
        post.setRoomTitle(boardDto.getRoomTitle());
        boardRepository.save(post);
    }
    @Transactional
    public List<BoardDto> getBoardList() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for(Board board : boardList) {
            BoardDto boardDto = BoardDto.builder()
                    .roomId(board.getRoomId())
                    .roomTitle(board.getRoomTitle())
                    .author(board.getAuthor())
                    .itemName(board.getItemName())
                    .recruitNum(board.getRecruitNum())
                    .currentNum(board.getCurrentNum())
                    .currentPrice(board.getCurrentPrice())
                    .totalPrice(board.getTotalPrice())
                    .itemPrice(board.getItemPrice())
                    .createdDate(board.getCreatedDate())
                    .siteName(board.getSiteName())
                    .member(board.getMember())
                    .build();
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    public BoardDto getPost(Integer id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            throw new EntityNotFoundException("Board not found with id: " + id);
        }
        Board board = optionalBoard.get();
        MyPageDto myPageDto = findOwnerMyPageByRoomId(board.getRoomId());

        BoardDto boardDto = BoardDto.builder()
                .roomId(board.getRoomId())
                .roomTitle(board.getRoomTitle())
                .author(board.getAuthor())
                .itemName(board.getItemName())
                .recruitNum(board.getRecruitNum())
                .currentNum(board.getCurrentNum())
                .currentPrice(board.getCurrentPrice())
                .totalPrice(board.getTotalPrice())
                .createdDate(board.getCreatedDate())
                .itemPrice(board.getItemPrice())
                .siteName(board.getSiteName())
                .fileId(board.getFileId())
                .member(board.getMember())
                .myPageDto(myPageDto)
                .build();

        System.out.println("BoardDto created with MyPageDto: " + boardDto.getMyPageDto().getBank() + ", " + boardDto.getMyPageDto().getAccount());

        return boardDto;
    }

    private MyPageDto findOwnerMyPageByRoomId(Integer roomId) {
        Optional<RoomMember> roomOwner = roomMemberRepository.findByRoom_RoomIdAndIsRoomOwnerTrue(roomId);
        if (!roomOwner.isPresent()) {
            throw new EntityNotFoundException("Room owner not found for room id: " + roomId);
        }
        Member owner = roomOwner.get().getMember();
        Optional<MyPage> myPageOptional = myPageRepository.findByMemberId(owner.getId());
        if (!myPageOptional.isPresent()) {
            throw new EntityNotFoundException("MyPage not found for member id: " + owner.getId());
        }
        MyPage myPage = myPageOptional.get();
        return new MyPageDto(myPage.getBank(), myPage.getAccount());
    }
    @Transactional
    public void deletePost(Integer id) {
        boardRepository.deleteById(id);
    }

    public void increaseParticipantCount(Integer id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        board.increaseCurrentNum();
        boardRepository.save(board);
    }

    public void decreaseParticipantCount(Integer id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id: " + id));
        board.setCurrentNum(board.getCurrentNum() - 1);
        boardRepository.save(board);
    }

    public boolean withdrawFromBoard(Integer boardId, String loginId) {

        Optional<Participant> participantOpt = participantRepository.findByBoardRoomIdAndMember_LoginId(boardId, loginId);

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
        List<Board> boards = boardRepository.findByRoomTitleContaining(title);
        return boards.stream().map(board -> BoardDto.builder()
                .roomId(board.getRoomId())
                .roomTitle(board.getRoomTitle())
                .author(board.getAuthor())
                .itemName(board.getItemName())
                .recruitNum(board.getRecruitNum())
                .currentNum(board.getCurrentNum())
                .currentPrice(board.getCurrentPrice())
                .totalPrice(board.getTotalPrice())
                .itemPrice(board.getItemPrice())
                .createdDate(board.getCreatedDate())
                .siteName(board.getSiteName())
                .fileId(board.getFileId())
                .build()).collect(Collectors.toList());
    }
}
