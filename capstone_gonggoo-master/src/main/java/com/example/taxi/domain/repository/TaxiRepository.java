package com.example.taxi.domain.repository;


import com.example.taxi.domain.entity.Taxi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxiRepository extends JpaRepository<Taxi, Integer> {
    List<Taxi> findByRoomTitleContaining(String roomTitle);
}