package com.ll.medium.member.login;

import com.ll.medium.member.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class LoginController {
    public static final String LOGIN_MEMBER = "LOGIN_MEMBER";
    private final LoginService loginService;

    // TODO: GET /member/login -> 로그인 요청
    @GetMapping("/login")
    public String login(LoginForm loginForm) {
        return "member/loginForm";
    }

    // TODO: POST /member/login -> 실제 로그인 로직 실행
    @PostMapping("/login")
    public String login(@Valid LoginForm loginForm, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "member/loginForm";
        }
        Member loginMember = loginService.login(loginForm.getUsername(), loginForm.getPassword());

        if (loginMember == null) {
            bindingResult.reject("singInFailed", "아이디 또는 비밀번호를 확인해주세요.");
            return "member/loginForm";
        }
        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_MEMBER, loginMember);

        return "redirect:"+redirectURL;
    }

    // TODO: POST /member/logout -> 실제 로그아웃 로직 실행
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
