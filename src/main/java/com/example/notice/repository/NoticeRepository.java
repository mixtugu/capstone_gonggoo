package com.example.notice.repository;

import com.example.notice.domain.Notice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class NoticeRepository {

  @PersistenceContext
  private EntityManager entityManager;

  public List<Notice> findAll() {
    return entityManager.createQuery("SELECT n FROM Notice n ORDER BY n.noticeNum DESC", Notice.class).getResultList();
  }

  public Optional<Notice> findById(Long id) {
    Notice notice = entityManager.find(Notice.class, id);
    return Optional.ofNullable(notice);
  }


}
