package com.example.service;

import com.example.domain.Room;
import com.example.domain.RoomMember;
import com.example.groupbuying.dto.MyPageDto;
import com.example.mypage.domain.MyPage;
import com.example.mypage.repository.MyPageRepository;
import com.example.repository.RoomMemberRepository;
import com.example.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    private RoomMemberRepository roomMemberRepository;
    @Autowired
    private MyPageRepository myPageRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    //룸id로 룸 조회
    public Room findOne(Integer room_id) {
        return roomRepository.findOne(room_id);
    }

    public MyPageDto findOwnerMyPageByRoomId(Integer roomId) {
        // Room의 owner를 찾는다.
        Optional<RoomMember> roomOwner = roomMemberRepository.findByRoom_RoomIdAndIsRoomOwnerTrue(roomId);

        if (roomOwner.isPresent()) {
            // Member 객체를 사용하여 MyPage 정보를 조회한다.
            Optional<MyPage> optionalMyPage = myPageRepository.findByMemberId(roomOwner.get().getMember().getId());

            if (optionalMyPage.isPresent()) {
                // DTO 변환 로직, 예를 들어 MyPageDto 반환
                return convertToMyPageDto(optionalMyPage.get());
            } else {
                throw new EntityNotFoundException("MyPage not found for the room owner");
            }
        }
        throw new EntityNotFoundException("Room owner not found");
    }

    private MyPageDto convertToMyPageDto(MyPage myPage) {
        return new MyPageDto(
                myPage.getBank(),
                myPage.getAccount()
        );
    }

}
