package com.wip.model.dto;

import com.wip.model.Comment;
import com.wip.model.Content;

import java.util.List;

public class ArticleInfo {
    private Content article;
    private List<Comment> comments;

    public ArticleInfo(Content article, List<Comment> comments) {
        this.article = article;
        this.comments = comments;
    }

    public Content getArticle() {
        return article;
    }

    public void setArticle(Content article) {
        this.article = article;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
