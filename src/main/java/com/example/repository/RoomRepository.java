package com.example.repository;

import com.example.domain.Member;
import com.example.domain.Room;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomRepository {

    private final EntityManager em;

    //룸 저장
    public void save(Room room) {
        em.persist(room);
    }

    //PK로 룸찾기
    public Room findOne(Integer roomId) {
        return em.find(Room.class, roomId);
    }

    //모든 룸 찾기
    public List<Room> findAll() {
        return em.createQuery("select m from Room m", Room.class)
                .getResultList();
    }

    //이름으로 룸 조회
    public List<Room> findByRoomTitle(String roomTitle) {
        return em.createQuery("select m from Room m where m.roomTitle = :roomTitle", Room.class)
                .setParameter("roomTitle", roomTitle)
                .getResultList();
    }

    //테스트 전용 메소드
    public void clear() {
        em.clear();
    }

}
