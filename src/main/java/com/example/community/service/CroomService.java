package com.example.community.service;

import com.example.community.domain.entity.Croom;
import com.example.community.domain.entity.Cparticipant;
import com.example.community.domain.repository.CparticipantRepository;
import com.example.community.domain.repository.CroomRepository;
import com.example.community.dto.CparticipantDto;
import com.example.community.dto.CroomDto;
import com.example.groupbuying.dto.BoardDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CroomService {
    private final CroomRepository croomRepository;
    private final CparticipantRepository cparticipantRepository;

    public CroomService(CroomRepository croomRepository, CparticipantRepository cparticipantRepository) {
        this.croomRepository = croomRepository;
        this.cparticipantRepository = cparticipantRepository;
    }

    @Transactional
    public Integer savePost(CroomDto croomDto) {
        if (croomDto.getAuthor() == null || croomDto.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("작성자 정보는 필수입니다.");
        }
        Croom croom = croomDto.toEntity();
        croom = croomRepository.save(croom);  // 저장된 Croom 엔티티를 반환받음
        return croom.getRoomId();  // Croom 엔티티의 ID를 반환
    }

    public List<CroomDto> getCroomList() {
        List<Croom> croomList = croomRepository.findAll();
        List<CroomDto> croomDtoList = new ArrayList<>();

        for(Croom croom : croomList) {
            CroomDto croomDto = CroomDto.builder()
                    .roomId(croom.getRoomId())
                    .author(croom.getAuthor())
                    .recruitNum(croom.getRecruitNum())
                    .currentNum(croom.getCurrentNum())
                    .roomTitle(croom.getRoomTitle())
                    .communityCategory(croom.getCommunityCategory())
                    .region(croom.getRegion())
                    .detailRegion(croom.getDetailRegion())
                    .payment(croom.getPayment())
                    .createdDate(croom.getCreatedDate())

                    .member(croom.getMember())
                    .build();
            croomDtoList.add(croomDto);
        }
        return croomDtoList;
    }

    public CroomDto getRoomDetails(Integer roomId) {
        return croomRepository.findById(roomId)
                .map(this::convertToDto)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
    }



    public List<CroomDto> searchRooms(String roomTitle, String communityCategory, String region) {
        if ((roomTitle == null || roomTitle.isEmpty()) &&
                (communityCategory == null || communityCategory.isEmpty()) &&
                (region == null || region.isEmpty())) {
            // 모든 입력 값이 비어 있을 경우 전체 목록을 반환
            return croomRepository.findAll().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
        // 하나라도 입력 값이 있는 경우 해당 조건에 맞는 데이터 검색
        return croomRepository.findBySearchCriteria(roomTitle, communityCategory, region).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public List<CroomDto> searchRoomsByDetail(String roomTitle, String detailCategory, String detailRegion) {
        if ((roomTitle == null || roomTitle.isEmpty()) &&
                (detailCategory == null || detailCategory.isEmpty()) &&
                (detailRegion == null || detailRegion.isEmpty())) {
            // 모든 입력 값이 비어 있을 경우 전체 목록을 반환
            return croomRepository.findAll().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
        // 하나라도 입력 값이 있는 경우 해당 조건에 맞는 데이터 검색
        return croomRepository.findByDetailCriteria(roomTitle, detailCategory, detailRegion).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



    public boolean isRoomTitleExists(String title) {
        return croomRepository.findByRoomTitle(title) != null;
    }

    public List<CroomDto> findByCommunityCategory(String communityCategory) {
        return croomRepository.findByCommunityCategory(communityCategory).stream()
                .map(this::convertToDto).collect(Collectors.toList());
    }

    public List<CroomDto> findByRegion(String region) {
        return croomRepository.findByRegion(region).stream()
                .map(this::convertToDto).collect(Collectors.toList());
    }

    public List<CroomDto> findByDetailCategory(String detailCategory) {
        return croomRepository.findByDetailCategory(detailCategory).stream()
                .map(this::convertToDto).collect(Collectors.toList());
    }

    public List<CroomDto> findByDetailRegion(String detailRegion) {
        return croomRepository.findByRegion(detailRegion).stream()
                .map(this::convertToDto).collect(Collectors.toList());
    }



    private CroomDto convertToDto(Croom croom) {
        return CroomDto.builder()
                .roomId(croom.getRoomId())
                .author(croom.getAuthor())
                .recruitNum(croom.getRecruitNum())
                .currentNum(croom.getCurrentNum())
                .roomCategory(croom.getRoomCategory())
                .roomTitle(croom.getRoomTitle())
                .communityCategory(croom.getCommunityCategory())
                .detailCategory(croom.getDetailCategory())
                .region(croom.getRegion())
                .detailRegion(croom.getDetailRegion())
                .payment(croom.getPayment())
                .member(croom.getMember())
                .build();
    }

    //추가중
    public CroomDto getPost(Integer id) {
        Croom croom = croomRepository.findById(id).get();
        CroomDto croomDto = CroomDto.builder()
                .roomId(croom.getRoomId())
                .author(croom.getAuthor())
                .recruitNum(croom.getRecruitNum())
                .currentNum(croom.getCurrentNum())
                .roomTitle(croom.getRoomTitle())

                .communityCategory(croom.getCommunityCategory())
                .detailCategory(croom.getDetailCategory())
                .region(croom.getRegion())
                .detailRegion(croom.getDetailRegion())
                .payment(croom.getPayment())


                .member(croom.getMember())
                .build();
        return croomDto;}


    public void deletePost(Integer id) {
        croomRepository.deleteById(id);
    }

    public void increaseCparticipantCount(Integer id) {
        Croom croom = croomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
    }



    // ID를 이용한 Croom 조회
    public Croom findById(Integer roomId) {
        Optional<Croom> croom = croomRepository.findById(roomId);
        if (croom.isPresent()) {
            return croom.get();
        } else {
            throw new IllegalArgumentException("Invalid room ID: " + roomId);
        }
    }


    // 방 수정
    public void updateRoom(Integer roomId, CroomDto croomDto) {
        Croom room = croomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
        room.updateFromDto(croomDto);
        croomRepository.save(room);
    }

    // 방 삭제
    public void deleteRoom(Integer roomId) {
        croomRepository.deleteById(roomId);
    }

    //서경원 구현부-진행중인공구리스트
    public List<CroomDto> getParticipatedCroomListByMemberId(Long memberId) {
        return cparticipantRepository.findByMemberId(memberId).stream()
            .map(cparticipant -> {
                Croom croom = cparticipant.getCroom();
                return new CroomDto(
                    croom.getRoomId(),
                    croom.getAuthor(),
                    croom.getRecruitNum(),
                    croom.getCurrentNum(),
                    croom.getRoomCategory(),
                    croom.getRoomTitle(),
                    croom.getCommunityCategory(),
                    croom.getDetailCategory(),
                    croom.getRegion(),
                    croom.getDetailRegion(),
                    croom.getPayment(),
                    croom.getCreatedDate(),
                    croom.getModifiedDate(),
                    croom.getMember()
                );
            })
            .collect(Collectors.toList());
    }

}