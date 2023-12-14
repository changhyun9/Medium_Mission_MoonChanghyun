package com.ll.medium.article.article;

import com.ll.medium.member.member.Member;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> findByArticles30() {
        List<Article> articleList = articleRepository.findTop30ByIsPublishedOrderByCreateTime("true");
        return articleList;
    }


    // TODO: Paging 처리
    public List<Article> findArticlesByIsPublished() {
        List<Article> articleList = articleRepository.findArticlesByIsPublishedOrderByCreateTime("true");
        return articleList;
    }

    // TODO: paging 처리
    public List<Article> findArticlesByAuthor(Member member) {
        List<Article> articleList = articleRepository.findArticlesByAuthor(member);
        return articleList;
    }

    public Article findById(Long id) {
        Optional<Article> findArticle = articleRepository.findById(id);
        if (findArticle.isPresent()) {
            Article article = findArticle.get();
            if (article.getIsPublished().equals("true")) {
                return article;
            }
            throw new NoSuchElementException("비공개 게시글 입니다.");
        }
        throw new NoSuchElementException("게시글이 없습니다.");
    }

    public Long createArticle(String title, String body, String isPublished, Member member) {
        Article article = new Article();
        article.setTitle(title);
        article.setBody(body);
        article.setIsPublished(isPublished);
        article.setAuthor(member);
        article.setCreateTime(LocalDateTime.now());
        Article save = articleRepository.save(article);
        return save.getId();
    }

    public void modifyArticle(Article article, String title, String body, String isPublished) {
        article.setTitle(title);
        article.setBody(body);
        article.setIsPublished(isPublished);
        articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
