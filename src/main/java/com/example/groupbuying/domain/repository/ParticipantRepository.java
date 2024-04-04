package com.example.groupbuying.domain.repository;

import com.example.domain.Member;
import com.example.groupbuying.domain.entity.Board;
import com.example.groupbuying.domain.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByBoardIdAndMember_LoginId(Long boardId, String loginId);
    List<Participant> findByBoard(Board board);
    Optional<Participant> findByBoardIdAndMember_LoginId(Long boardId, String loginId);

    Optional<Participant> findById(Long id);

    Participant findByMember_Name(String name);

}
