package com.example.service;

import com.example.domain.Member;
import com.example.mypage.domain.MyPage;
import com.example.mypage.repository.MyPageRepository;
import com.example.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MyPageRepository myPageRepository;
    /**
     * 회원 가입
     */
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        //마이페이지 추가부분-서경원
        MyPage myPage = new MyPage();
        myPage.setMember(member);
        myPageRepository.save(myPage);
        //---------------------여기까지
        return member.getId();
    }

    //이름,로그인아이디 중복 조회
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        Optional<Member> findOne = memberRepository.findByLoginId(member.getLoginId());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("error");
        }
        if (!findOne.isEmpty()) {
            // 해당 로그인 아이디를 가진 멤버가 존재하지 않는 경우
            throw new IllegalArgumentException("Member not found for login ID;");
        }
    }

    //멤버 전제 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //멤버아이디로 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
