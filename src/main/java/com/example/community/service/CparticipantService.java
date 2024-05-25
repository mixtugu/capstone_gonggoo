package com.example.community.service;

import com.example.domain.Member;
import com.example.community.domain.entity.Croom;
import com.example.community.domain.entity.Cparticipant;
import com.example.community.domain.repository.CroomRepository;
import com.example.community.domain.repository.CparticipantRepository;
import com.example.community.dto.CparticipantDto;
import com.example.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CparticipantService {

    private final CparticipantRepository cparticipantRepository;
    private final MemberRepository memberRepository;
    private final CroomRepository croomRepository;

    public CparticipantService(CparticipantRepository cparticipantRepository, MemberRepository memberRepository, CroomRepository croomRepository) {
        this.cparticipantRepository = cparticipantRepository;
        this.memberRepository = memberRepository;
        this.croomRepository = croomRepository;
    }


    @Transactional
    public Integer addParticipant(Integer croomId, String loginId, boolean isRoomOwner) {

        Croom croom = croomRepository.findById(croomId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id=" + croomId));
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. loginId=" + loginId));

        Cparticipant cparticipant = Cparticipant.builder()
                .member(member)
                .croom(croom)
                .isRoomOwner(isRoomOwner)
                .build();

        //인원수 추가하는 코드 0523
        croom.setCurrentNum(croom.getCurrentNum() + 1);
        croomRepository.save(croom);


        cparticipantRepository.save(cparticipant);
        return cparticipant.getRoomMemberId();
    }

    public List<Cparticipant> getCparticipantsByCroomId(Integer croomId) {
        Croom croom = croomRepository.findById(croomId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id=" + croomId));
        return cparticipantRepository.findByCroom(croom);
    }

    public List<CparticipantDto> getCparticipantDtosByCroomId(Integer croomId) {
        Croom croom = croomRepository.findById(croomId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id=" + croomId));
        List<Cparticipant> cparticipants = cparticipantRepository.findByCroom(croom);
        return cparticipants.stream()
                .map(CparticipantDto::fromEntity)
                .collect(Collectors.toList());
    }

    public boolean isUserParticipated(Integer croomId, String loginId) {
        return cparticipantRepository.existsByCroomRoomIdAndMember_LoginId(croomId, loginId);
    }
    @Transactional
    public boolean removeCparticipant(Integer croomId, String loginId) {
        Cparticipant cparticipant = cparticipantRepository.findByCroomRoomIdAndMember_LoginId(croomId, loginId)
                .orElseThrow(() -> new IllegalArgumentException("해당 참여 정보를 찾을 수 없습니다. croomId: " + croomId + ", loginId: " + loginId));

        cparticipantRepository.delete(cparticipant);

        Croom croom = cparticipant.getCroom();
        croom.setCurrentNum(croom.getCurrentNum() - 1);
        croomRepository.save(croom);

        return false;
    }


    /*@Transactional
    public boolean withdrawFromCroom(Integer roomId, String loginId) {
        try {
            Cparticipant cparticipant = cparticipantRepository.findByCroomRoomIdAndMember_LoginId(roomId, loginId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 참여 정보를 찾을 수 없습니다. boardId: " + roomId + ", loginId: " + loginId));

            Croom croom = cparticipant.getCroom();

            cparticipantRepository.delete(cparticipant);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }*/
}