package com.example.groupbuying.domain.repository;


import com.example.groupbuying.domain.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}