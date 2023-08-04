package com.lim1t.board.service;

import com.lim1t.board.entity.Member;
import com.lim1t.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> findMemberByEmail(String userEmail) {
        return memberRepository.findMemberByUserEmail(userEmail);
    }
}
