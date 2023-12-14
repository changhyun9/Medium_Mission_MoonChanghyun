package com.ll.medium.article.article;

import com.ll.medium.member.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findTop30ByIsPublishedOrderByCreateTime(String isPublished);

    List<Article> findArticlesByIsPublishedOrderByCreateTime(String isPublished);

    List<Article> findArticlesByAuthor(Member member);
}
