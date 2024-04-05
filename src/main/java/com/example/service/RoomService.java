package com.example.service;

import com.example.domain.Room;
import com.example.repository.RoomMemberRepository;
import com.example.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    //룸id로 룸 조회
    public Room findOne(Integer room_id) {
        return roomRepository.findOne(room_id);
    }
}
