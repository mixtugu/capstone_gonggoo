package com.example.taxi.service;

import com.example.taxi.domain.entity.Taxi;
import com.example.taxi.domain.repository.TaxiRepository;
import com.example.taxi.domain.repository.TaxiParticipantRepository;
import com.example.taxi.dto.TaxiDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaxiService {
    private final TaxiRepository taxiRepository;
    private final TaxiParticipantRepository taxiParticipantRepository;

    public TaxiService(TaxiRepository taxiRepository, TaxiParticipantRepository taxiParticipantRepository) {
        this.taxiRepository = taxiRepository;
        this.taxiParticipantRepository = taxiParticipantRepository;
    }

    @Transactional
    public Integer savePost(TaxiDto taxiDto) {
        return taxiRepository.save(taxiDto.toEntity()).getRoomId();
    }
    @Transactional
    public List<TaxiDto> getTaxiList() {
        List<Taxi> taxiList = taxiRepository.findAll();
        List<TaxiDto> taxiDtoList = new ArrayList<>();

        for(Taxi taxi : taxiList) {
            TaxiDto taxiDto = TaxiDto.builder()
                    .roomId(taxi.getRoomId())
                    .roomTitle(taxi.getRoomTitle())
                    .author(taxi.getAuthor())
                    .recruitNum(taxi.getRecruitNum())
                    .currentNum(taxi.getCurrentNum())
                    .predicttotalprice(taxi.getPredicttotalprice())
                    .createdDate(taxi.getCreatedDate())
                    .departure(taxi.getDeparture())
                    .destination(taxi.getDestination())
                    .member(taxi.getMember())
                    .build();
            taxiDtoList.add(taxiDto);
        }
        return taxiDtoList;
    }

    public TaxiDto getPost(Integer id) {
        Taxi taxi = taxiRepository.findById(id).get();

        return TaxiDto.builder()
                .roomId(taxi.getRoomId())
                .roomTitle(taxi.getRoomTitle())
                .author(taxi.getAuthor())
                .recruitNum(taxi.getRecruitNum())
                .currentNum(taxi.getCurrentNum())
                .predicttotalprice(taxi.getPredicttotalprice())
                .createdDate(taxi.getCreatedDate())
                .departure(taxi.getDeparture())
                .destination(taxi.getDestination())
                .member(taxi.getMember())
                .build();
    }

    @Transactional
    public void deletePost(Integer id) {
        taxiRepository.deleteById(id);
    }

    public void increaseParticipantCount(Integer id) {
        Taxi taxi = taxiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        taxi.increaseCurrentNum();
        taxiRepository.save(taxi);
    }

    public void decreaseParticipantCount(Integer id) {
        Taxi taxi = taxiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id: " + id));
        taxi.setCurrentNum(taxi.getCurrentNum() - 1);
        taxiRepository.save(taxi);
    }

    public void updateTaxiInfo(Integer id, TaxiDto taxiDto) {
        Taxi post = taxiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id: " + id));

        post.setCurrentNum(taxiDto.getRecruitNum());
        post.setDeparture(taxiDto.getDeparture());
        post.setCreatedDate(taxiDto.getCreatedDate());
        post.setModifiedDate(taxiDto.getModifiedDate());
        post.setRecruitNum(taxiDto.getRecruitNum());
        post.setPredicttotalprice(taxiDto.getPredicttotalprice());
        post.setRoomTitle(taxiDto.getRoomTitle());
        post.setDestination(taxiDto.getDestination());
        taxiRepository.save(post);
    }

//    public boolean withdrawFromTaxi(Integer taxiId, String loginId) {
//
//        Optional<Participant> participantOpt = participantRepository.findByTaxiRoomIdAndMember_LoginId(taxiId, loginId);
//
//        if (participantOpt.isEmpty()) {
//            return false;
//        }
//
//        Participant participant = participantOpt.get();
//        Taxi taxi = participant.getTaxi();
//
//        taxi.setCurrentPrice(taxi.getCurrentPrice() - (participant.getQuantity() * taxi.getItemPrice()));
//        taxiRepository.save(taxi);
//        participantRepository.delete(participant);
//
//        return true;
//    }

    public List<TaxiDto> searchByTitle(String title) {
        List<Taxi> taxis = taxiRepository.findByRoomTitleContaining(title);
        return taxis.stream().map(taxi -> TaxiDto.builder()
                .roomId(taxi.getRoomId())
                .roomTitle(taxi.getRoomTitle())
                .author(taxi.getAuthor())
                .recruitNum(taxi.getRecruitNum())
                .currentNum(taxi.getCurrentNum())
                .predicttotalprice(taxi.getPredicttotalprice())
                .createdDate(taxi.getCreatedDate())
                .departure(taxi.getDeparture())
                .destination(taxi.getDestination())
                .build()).collect(Collectors.toList());
    }

    public TaxiParticipantRepository getParticipantRepository() {
        return taxiParticipantRepository;
    }
}
