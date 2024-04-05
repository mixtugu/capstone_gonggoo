package com.example.groupbuying.domain.repository;


import com.example.groupbuying.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByTitleContaining(String title);
}