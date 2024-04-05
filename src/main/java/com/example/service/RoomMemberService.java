package com.example.service;

import com.example.domain.Room;
import com.example.domain.RoomMember;
import com.example.repository.RoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomMemberService {
    private final RoomMemberRepository roomMemberRepository;

    //멤버id로 룸id 조회
    public List<Room> findRoomByMemberId(long member_id) {
        return roomMemberRepository.findRoomByMemberId(member_id);
    }
}
