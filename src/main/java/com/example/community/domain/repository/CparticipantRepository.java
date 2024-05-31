package com.example.community.domain.repository;

import com.example.community.domain.entity.Croom;
import com.example.community.domain.entity.Cparticipant;
import com.example.domain.Member;
import com.example.groupbuying.domain.entity.Board;
import com.example.groupbuying.domain.entity.Participant;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CparticipantRepository extends JpaRepository<Cparticipant, Integer> {
    boolean existsByCroomRoomIdAndMember_LoginId(Integer croomId, String loginId);
    List<Cparticipant> findByCroom(Croom croom);
    Optional<Cparticipant> findByCroomRoomIdAndMember_LoginId(Integer croomId, String loginId);


    Cparticipant findByMember_Name(String name);

  //서경원 추가부
   List<Cparticipant> findByMemberId(Long memberId);

}