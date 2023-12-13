package com.ll.medium.member.member;

import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long join(String username, String password) {
        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password);

        memberRepository.save(member);
        return member.getId();
    }

    public Member findMemberById(Long id) {
        Optional<Member> findMember = memberRepository.findById(id);
        if (findMember.isPresent()) {
            return findMember.get();
        }
        throw new NoSuchElementException("Not exist Member");
    }

    public Member findMemberByUsername(String username) {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        if (findMember.isPresent()) {
            return findMember.get();
        }
        throw new NoSuchElementException("Not exist Member");
    }

}
