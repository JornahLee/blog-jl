package com.wip.model.dto;

import com.wip.model.Comment;
import com.wip.model.Content;

import java.util.List;

public class ArticleInfo {
    // 一篇文章 可以有多个标签 多个分类，所以标签和分类重复了
    // 需要的信息 id ， 标题，内容主体，状态，作者，日期，创建时间，修改时间，播放量，rank值，是否允许评论，打赏
    // 保留草稿，可能会有许多冗余信息，所以新增草稿审计，版本号，如果需要发布，则将草稿内容同步到conten表，
    // 草稿表 外键 content表的id，
    // 附加信息，异步加载？ 评论 等

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
