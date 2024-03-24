package com.example.groupbuying.domain.repository;

import com.example.domain.Member;
import com.example.groupbuying.domain.entity.Board;
import com.example.groupbuying.domain.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByMemberAndBoard(Member member, Board board);
    List<Participant> findByBoard(Board board);
}
