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

        int amountToAdd = board.getItemPrice() * quantity;
        board.addPrice(amountToAdd);

        Participant participant = Participant.builder()
                .member(member)
                .board(board)
                .quantity(quantity)
                .build();

        participantRepository.save(participant);
        return participant.getId();
    }

    @Transactional
    public void updateQuantity(Long boardId, String loginId, Integer newQuantity) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id=" + boardId));
        Participant participant = participantRepository.findByBoardIdAndMember_LoginId(boardId, loginId)
                .orElseThrow(() -> new IllegalArgumentException("참가자가 존재하지 않습니다. loginId=" + loginId));

        int quantityDifference = newQuantity - participant.getQuantity();

        participant.updateQuantity(newQuantity);

        int amountToUpdate = board.getItemPrice() * quantityDifference;
        board.updatePrice(amountToUpdate);
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

    public boolean isUserParticipated(Long boardId, String loginId) {
        return participantRepository.existsByBoardIdAndMember_LoginId(boardId, loginId);
    }

    public void removeParticipant(Long boardId, String loginId) {
        Participant participant = participantRepository.findByBoardIdAndMember_LoginId(boardId, loginId)
                .orElseThrow(() -> new IllegalArgumentException("해당 참여 정보를 찾을 수 없습니다. boardId: " + boardId + ", loginId: " + loginId));
        participantRepository.delete(participant);
    }

    @Transactional
    public boolean withdrawFromBoard(Long boardId, String loginId) {
        try {
            Participant participant = participantRepository.findByBoardIdAndMember_LoginId(boardId, loginId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 참여 정보를 찾을 수 없습니다. boardId: " + boardId + ", loginId: " + loginId));

            Board board = participant.getBoard();
            int amountToSubtract = board.getItemPrice() * participant.getQuantity();
            board.subtractPrice(amountToSubtract);

            participantRepository.delete(participant);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public Long editParticipantQuantity(Long participantId, Integer newQuantity, String loginId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("참여자 정보를 찾을 수 없습니다."));

        if (!participant.getMember().getLoginId().equals(loginId)) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }

        Board board = participant.getBoard();
        int oldTotalPrice = participant.getQuantity() * board.getItemPrice();
        int newTotalPrice = newQuantity * board.getItemPrice();

        board.updatePrice(board.getCurrentPrice() - oldTotalPrice + newTotalPrice);
        participant.setQuantity(newQuantity);

        return board.getId();
    }

    public void updateParticipantQuantity(String name, int newQuantity) {
        Participant participant = participantRepository.findByMember_Name(name);
        if (participant != null) {
            participant.setQuantity(newQuantity);
            participantRepository.save(participant);
        }
    }

}

