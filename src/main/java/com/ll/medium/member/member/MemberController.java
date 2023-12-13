package com.ll.medium.member.member;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // TODO: GET /member/join -> 회원 가입 요청
    @GetMapping("/join")
    public String join(MemberForm memberForm) {
        return "member/joinForm";
    }

    // TODO: POST /member/join -> 실제 회원 가입 로직 실행
    @PostMapping("/join")
    public String join(@Valid MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/joinForm";
        }

        if (!memberForm.getPassword().equals(memberForm.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "passwordIncorrect",
                    "비밀번호가 일치하지 않습니다.");
            return "member/joinForm";
        }
        try {
            memberService.join(memberForm.getUsername(), memberForm.getPassword());
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("singupFaild", "아이디가 중복되었습니다.");
            return "member/joinForm";
        } catch (Exception e) {
            bindingResult.reject("signupFailed", "회원가입에 실패하였습니다");
            return "member/joinForm";
        }

        return "redirect:/";

    }
}
