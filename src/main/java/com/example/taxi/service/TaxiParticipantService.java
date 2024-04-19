package com.example.taxi.service;

import com.example.domain.Member;
import com.example.repository.MemberRepository;
import com.example.taxi.domain.entity.Taxi;
import com.example.taxi.domain.entity.TaxiParticipant;
import com.example.taxi.domain.repository.TaxiRepository;
import com.example.taxi.domain.repository.TaxiParticipantRepository;
import com.example.taxi.dto.ParticipantDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaxiParticipantService {

    private final TaxiParticipantRepository taxiParticipantRepository;
    private final MemberRepository memberRepository;
    private final TaxiRepository taxiRepository;

    public TaxiParticipantService(TaxiParticipantRepository taxiParticipantRepository, MemberRepository memberRepository, TaxiRepository taxiRepository) {
        this.taxiParticipantRepository = taxiParticipantRepository;
        this.memberRepository = memberRepository;
        this.taxiRepository = taxiRepository;
    }

    @Transactional
    public Integer addParticipant(Integer taxiId, String loginId, boolean isRoomOwner) {

        Taxi taxi = taxiRepository.findById(taxiId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id=" + taxiId));
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. loginId=" + loginId));

//        int amountToAdd = taxi.getItemPrice() * quantity;
//        taxi.addPrice(amountToAdd);

        TaxiParticipant taxiParticipant = TaxiParticipant.builder()
                .member(member)
                .taxi(taxi)
                .isRoomOwner(isRoomOwner)
                .build();

        taxiParticipantRepository.save(taxiParticipant);
        return taxiParticipant.getRoomMemberId();
    }

//    @Transactional
//    public void updateQuantity(Integer taxiId, String loginId, Integer newQuantity) {
//        Taxi taxi = taxiRepository.findById(taxiId)
//                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id=" + taxiId));
//        TaxiParticipant taxiParticipant = taxiParticipantRepository.findByTaxiRoomIdAndMember_LoginId(taxiId, loginId)
//                .orElseThrow(() -> new IllegalArgumentException("참가자가 존재하지 않습니다. loginId=" + loginId));
//    }

    public List<TaxiParticipant> getParticipantsByTaxiId(Integer taxiId) {
        Taxi taxi = taxiRepository.findById(taxiId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id=" + taxiId));
        return taxiParticipantRepository.findByTaxi(taxi);
    }

    public List<ParticipantDto> getParticipantDtosByTaxiId(Integer taxiId) {
        Taxi taxi = taxiRepository.findById(taxiId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id=" + taxiId));
        List<TaxiParticipant> taxiParticipants = taxiParticipantRepository.findByTaxi(taxi);
        return taxiParticipants.stream()
                .map(ParticipantDto::fromEntity)
                .collect(Collectors.toList());
    }

    public boolean isUserParticipated(Integer taxiId, String loginId) {
        return taxiParticipantRepository.existsByTaxiRoomIdAndMember_LoginId(taxiId, loginId);
    }

    public void removeParticipant(Integer taxiId, String loginId) {
        TaxiParticipant taxiParticipant = taxiParticipantRepository.findByTaxiRoomIdAndMember_LoginId(taxiId, loginId)
                .orElseThrow(() -> new IllegalArgumentException("해당 참여 정보를 찾을 수 없습니다. taxiId: " + taxiId + ", loginId: " + loginId));
        taxiParticipantRepository.delete(taxiParticipant);
    }

//    @Transactional
//    public boolean withdrawFromTaxi(Integer taxiId, String loginId) {
//        try {
//            Participant participant = participantRepository.findByTaxiRoomIdAndMember_LoginId(taxiId, loginId)
//                    .orElseThrow(() -> new IllegalArgumentException("해당 참여 정보를 찾을 수 없습니다. taxiId: " + taxiId + ", loginId: " + loginId));
//
//            Taxi taxi = participant.getTaxi();
//            int amountToSubtract = taxi.getItemPrice() * participant.getQuantity();
//            taxi.subtractPrice(amountToSubtract);
//
//            participantRepository.delete(participant);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

//    @Transactional
//    public Integer editParticipantQuantity(Integer participantId, Integer newQuantity, String loginId) {
//        Participant participant = participantRepository.findById(participantId)
//                .orElseThrow(() -> new IllegalArgumentException("참여자 정보를 찾을 수 없습니다."));
//
//        if (!participant.getMember().getLoginId().equals(loginId)) {
//            throw new IllegalStateException("수정 권한이 없습니다.");
//        }
//
//        Taxi taxi = participant.getTaxi();
//        int oldTotalPrice = participant.getQuantity() * taxi.getItemPrice();
//        int newTotalPrice = newQuantity * taxi.getItemPrice();
//
//        taxi.updatePrice(taxi.getCurrentPrice() - oldTotalPrice + newTotalPrice);
//        participant.setQuantity(newQuantity);
//
//        return taxi.getRoomId();
//    }

//    public void updateParticipantQuantity(String name, int newQuantity) {
//        TaxiParticipant taxiParticipant = taxiParticipantRepository.findByMember_Name(name);
//        if (taxiParticipant != null) {
//            taxiParticipantRepository.save(taxiParticipant);
//        }
//    }

}

