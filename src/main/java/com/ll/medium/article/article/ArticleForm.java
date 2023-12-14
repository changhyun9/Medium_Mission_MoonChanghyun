package com.ll.medium.article.article;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ArticleForm {

    @NotEmpty
    @Length(max=200)
    private String title;
    @NotEmpty
    private String body;
    @NotEmpty
    private String isPublished;
}
