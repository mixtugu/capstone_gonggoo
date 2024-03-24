package com.example.groupbuying.service;

import com.example.domain.Member;
import com.example.groupbuying.domain.entity.Board;
import com.example.groupbuying.domain.entity.Participant;
import com.example.groupbuying.domain.repository.BoardRepository;
import com.example.groupbuying.domain.repository.ParticipantRepository;
import com.example.groupbuying.dto.ParticipantDto;
import com.example.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public ParticipantService(ParticipantRepository participantRepository, MemberRepository memberRepository, BoardRepository boardRepository) {
        this.participantRepository = participantRepository;
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Long addParticipant(Long boardId, String loginId, Integer quantity) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id=" + boardId));
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. loginId=" + loginId));

        int amountToAdd = board.getItemPrice() * quantity; // getItemPrice() 메서드는 Board 엔티티에 정의되어 있다고 가정
        board.addPrice(amountToAdd); // Board 엔티티에 addPrice 메서드 추가 필요, 현재 가격에 금액 추가

        Participant participant = Participant.builder()
                .member(member)
                .board(board)
                .quantity(quantity)
                .build();

        participantRepository.save(participant);
        return participant.getId();
    }

    public List<Participant> getParticipantsByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id=" + boardId));
        return participantRepository.findByBoard(board);
    }

    public List<ParticipantDto> getParticipantDtosByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id=" + boardId));
        List<Participant> participants = participantRepository.findByBoard(board);
        return participants.stream()
                .map(ParticipantDto::fromEntity)
                .collect(Collectors.toList());
    }
}

