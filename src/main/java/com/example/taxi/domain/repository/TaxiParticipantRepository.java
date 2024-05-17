package com.example.taxi.domain.repository;

import com.example.taxi.domain.entity.Taxi;
import com.example.taxi.domain.entity.TaxiParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxiParticipantRepository extends JpaRepository<TaxiParticipant, Integer> {
    boolean existsByTaxiRoomIdAndMember_LoginId(Integer taxiId, String loginId);
    List<TaxiParticipant> findByTaxi(Taxi taxi);
    Optional<TaxiParticipant> findByTaxiRoomIdAndMember_LoginId(Integer taxiId, String loginId);

    Optional<TaxiParticipant> findByTaxiRoomId(Integer taxiId);

    //서경원 추가부
    List<TaxiParticipant> findByMemberId(Long memberId);


}
