package com.example.community.domain.repository;

import com.example.community.domain.entity.Croom;
import com.example.community.domain.entity.Cparticipant;
import com.example.domain.Member;
import com.example.groupbuying.domain.entity.Board;
import com.example.groupbuying.domain.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CparticipantRepository extends JpaRepository<Cparticipant, Integer> {
    boolean existsByCroomRoomIdAndMember_LoginId(Integer croomId, String loginId);
    List<Cparticipant> findByCroom(Croom croom);
    Optional<Cparticipant> findByCroomRoomIdAndMember_LoginId(Integer croomId, String loginId);
    Optional<Cparticipant> findByCroomAndMember(Croom croom, Member member);
    Optional<Cparticipant> findByCroomRoomId(Integer croomId);

    Cparticipant findByMember_Name(String name);

}