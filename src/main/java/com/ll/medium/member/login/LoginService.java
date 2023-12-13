package com.ll.medium.member.login;

import com.ll.medium.member.member.Member;
import com.ll.medium.member.member.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public Member login(String username, String password) {
        Optional<Member> findMember = memberRepository.findByUsername(username);

        if (findMember.isPresent()) {
            Member member = findMember.get();
            if (member.getPassword().equals(password)) {
                return member;
            }
            return null;
        }
        return null;
    }
}
