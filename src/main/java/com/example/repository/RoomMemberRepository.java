package com.example.repository;

import com.example.domain.Member;
import com.example.domain.Room;
import com.example.domain.RoomMember;
import com.example.service.MemberService;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import jakarta.persistence.PersistenceContext;

@Repository
public class RoomMemberRepository {

    private final MemberService memberService;

    @PersistenceContext
    private EntityManager em;

    public RoomMemberRepository(MemberService memberService) {
        this.memberService = memberService;
    }

    // Add a new RoomMember
    public void addRoomMember(RoomMember roomMember) {
        em.persist(roomMember);
    }

    // Find a RoomMember by RoomMemberID 고유키로 찾기
    public RoomMember findRoomMemberById(int roomMemberId) {
        return em.find(RoomMember.class, roomMemberId);
    }

    // Find RoomMembers by RoomID 룸 id로 찾기
    public List<RoomMember> findRoomMembersByRoomId(int roomId) {
        return em.createQuery("SELECT rm FROM RoomMember rm WHERE rm.room = :roomId", RoomMember.class)
                .setParameter("roomId", roomId)
                .getResultList();
    }

    //멤버 id로 룸 id 찾기
    public List<Room> findRoomByMemberId(long memberid) {
        //Member member = memberService.findOne(member_id);
        return em.createQuery("SELECT rm.room FROM RoomMember rm WHERE rm.member.id = :memberid", Room.class)
                .setParameter("memberid", memberid)
                .getResultList();
    }

    // Update a RoomMember's information
    public void updateRoomMember(RoomMember roomMember) {
        em.merge(roomMember);
    }

    // Delete a RoomMember by RoomMemberID
    public void deleteRoomMemberById(int roomMemberId) {
        RoomMember roomMember = findRoomMemberById(roomMemberId);
        if (roomMember != null) {
            em.remove(roomMember);
        }
    }

    // Utility method to clear persistence context (for testing)
    public void clear() {
        em.clear();
    }

    public Optional<RoomMember> findByRoom_RoomIdAndIsRoomOwnerTrue(int roomId) {
            RoomMember roomMember = em.createQuery("SELECT rm FROM RoomMember rm WHERE rm.room.id = :roomId AND rm.isRoomOwner = true", RoomMember.class)
                    .setParameter("roomId", roomId)
                    .getSingleResult();
            return Optional.of(roomMember);
    }
}

