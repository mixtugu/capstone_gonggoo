package com.example.repository;


import com.example.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    //멤버 저장
    public void save(Member member) {
        em.persist(member);
    }

    //PK로 멤버찾기
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    //모든 멤버 찾기
    public List<Member> findAll() {
        return em.createQuery("select  m from Member  m", Member.class)
                .getResultList();
    }

    //이름으로 멤버 조회
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    //로그인 아이디로 멤버 조회
    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    //테스트 전용 메소드
    public void clear() {
        em.clear();
    }

}