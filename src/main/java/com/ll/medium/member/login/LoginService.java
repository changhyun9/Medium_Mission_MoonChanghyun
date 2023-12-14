package com.ll.medium.member.login;

import com.ll.medium.member.member.Member;
import com.ll.medium.member.member.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    public static final String LOGIN_MEMBER = "LOGIN_MEMBER";
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

    public Member getLoginMember(HttpServletRequest request) {
        return (Member) request.getSession(false).getAttribute(LOGIN_MEMBER);
    }
}
