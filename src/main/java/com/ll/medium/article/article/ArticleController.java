package com.ll.medium.article.article;

import com.ll.medium.member.login.LoginService;
import com.ll.medium.member.member.Member;
import com.ll.medium.member.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final MemberService memberService;
    private final LoginService loginService;

    // TODO: GET / -> 홈화면 이동, 최신글 30개 노출
    @GetMapping("/")
    public String home(Model model) {
        List<Article> articleList = articleService.findByArticles30();
        model.addAttribute("articleList", articleList);
        return "home";
    }

    // TODO: GET /post/list -> 글 목록 조회, 공개된 글만 노출, paging 처리
    @GetMapping("/post/list")
    public String showArticleList(Model model) {
        List<Article> articleList = articleService.findArticlesByIsPublished();
        model.addAttribute("articleList", articleList);
        return "article/board";
    }

    // TODO: GET /post/myList -> 내 글 목록 조회, 공개여부와 관계없이 모두
    @GetMapping("/post/myList")
    public String showMyArticleList(Model model, HttpServletRequest request) {
            Member loginMember = loginService.getLoginMember(request);
            List<Article> articleList = articleService.findArticlesByAuthor(loginMember);
            model.addAttribute("articleList", articleList);

        return "article/myArticles";
    }

    // TODO: GET /post/{id} -> id번 글 상세조회
    @GetMapping("/post/{id}")
    public String articleDetail(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        try {
            Member loginMember = loginService.getLoginMember(request);
            model.addAttribute("loginMember", loginMember);
            Article article = articleService.findById(id);
            model.addAttribute("article", article);
        } catch (NoSuchElementException e) {
            // 에러 메세지 e.getMessage() 해서 경고문 띄우고, 돌아가기
            return "redirect:/post/list";
        }
        return "article/detail";
    }

    // TODO: GET /post/write -> 글 작성 폼 이동
    @GetMapping("/post/write")
    public String write(ArticleForm articleForm, HttpServletRequest request) {
        Member loginMember = loginService.getLoginMember(request);
        return "article/writeForm";
    }

    // TODO: POST /post/write -> 글 작성 처리, 실제 작성 로직 실행
    @PostMapping("/post/write")
    public String write(@Valid ArticleForm articleForm, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "article/writeForm";
        }
            Member loginMember = loginService.getLoginMember(request);
            Long id = articleService.createArticle(articleForm.getTitle(), articleForm.getBody(),
                    articleForm.getIsPublished(), loginMember);
        return "redirect:/post/"+id;
    }

    // TODO: GET /post/{id}/modify -> id번 글 수정 폼 이동
    @GetMapping("/post/{id}/modify")
    public String modify(@PathVariable("id") Long id, ArticleForm articleForm, HttpServletRequest request) {
        try {
            Member loginMember = loginService.getLoginMember(request);
            Article findArticle = articleService.findById(id);
            if (loginMember.equals(findArticle.getAuthor())) {
                articleForm.setTitle(findArticle.getTitle());
                articleForm.setBody(findArticle.getBody());
                articleForm.setIsPublished(findArticle.getIsPublished());
                return "article/writeForm";
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        } catch (NoSuchElementException e) {
            // 로그인 하라고 모듈 창 띄우기 또는 에러글 띄우기
            return "redirect:/post/list";
        } catch (ResponseStatusException e) {
            // 수정 권한이 없다고 띄우기, 위의 catch문이랑 합칠 생각
            return "redirect:/post/list";
        }
    }

    // TODO: POST /post/{id}/modify -> 글 수정 처리, 실제 수정 로직 실행
    @PostMapping("/post/{id}/modify")
    public String modify(@PathVariable("id") Long id, @Valid ArticleForm articleForm,
                         BindingResult bindingResult, HttpServletRequest request) {
        try {
            Member loginMember = loginService.getLoginMember(request);
            Article findArticle = articleService.findById(id);

            if (!loginMember.equals(findArticle.getAuthor())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            }
            if (bindingResult.hasErrors()) {
                return "article/writeForm";
            }
            articleService.modifyArticle(findArticle, articleForm.getTitle(), articleForm.getBody(),
                    articleForm.getIsPublished());
            return "redirect:/post/" + id;
        } catch (NoSuchElementException e) {
            // 로그인 하라고 모듈 창 띄우기 또는 에러글 띄우기
            return "redirect:/post/list";
        } catch (ResponseStatusException e) {
            // 수정 권한이 없다고 띄우기, 위의 catch문이랑 합칠 생각
            return "redirect:/post/list";
        }
    }

    // TODO: DELETE /post/{id}/delete -> id번 글 삭제, 실제 삭제 로직 실행
    @DeleteMapping("/post/{id}/delete")
    public String delete(@PathVariable("id") Long id, HttpServletRequest request) {
        try {
            Member loginMember = loginService.getLoginMember(request);
            Article findArticle = articleService.findById(id);

            if (!loginMember.equals(findArticle.getAuthor())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            }
            articleService.deleteArticle(id);
            return "redirect:/post/list";
        } catch (NoSuchElementException e) {
            // 로그인 하라고 모듈 창 띄우기 또는 에러글 띄우기
            return "redirect:/post/list";
        } catch (ResponseStatusException e) {
            // 수정 권한이 없다고 띄우기, 위의 catch문이랑 합칠 생각
            return "redirect:/post/list";
        }
    }

    // TODO: GET /b/{username} -> username에 해당하는 유저가 쓴 전체 글 조회
    @GetMapping("/b/{username}")
    public String showUserAricleList(@PathVariable String username, Model model) {
        try {
            Member findMember = memberService.findMemberByUsername(username);
            List<Article> articleList = articleService.findArticlesByAuthor(findMember);
            model.addAttribute("articleList", articleList);
        } catch (NoSuchElementException e) {
            // 해당 회원이 없다고 출력
            return "redirect:/";
        }

        return "article/board";
    }

    // TODO: GET /b/{username}/{id} -> username에 해당하는 유저가 쓴 id번 글 조회
    @GetMapping("/b/{username}/{id}")
    public String showUserArticle(@PathVariable String username, @PathVariable("id") Long id, Model model) {
        try {
            Member findMember = memberService.findMemberByUsername(username);
            List<Article> articleList = articleService.findArticlesByAuthor(findMember);
            for (Article article : articleList) {
                if (article.getId() == id) {
                    model.addAttribute("article", article);
                    return "article/detail";
                }
            }
        } catch (NoSuchElementException e) {
            // 해당 회원이 없거나
            return "redirect:/";
        }
        // 해당 번호가 없는 경우
        return "redirect:/b/"+username;
    }
}
