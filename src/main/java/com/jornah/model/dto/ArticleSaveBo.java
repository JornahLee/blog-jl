package com.jornah.model.dto;

import com.jornah.model.newP.Article;
import lombok.Data;

import java.util.List;

@Data
public class ArticleSaveBo {
    // 一篇文章 可以有多个标签 多个分类，所以标签和分类重复了
    // 需要的信息 id ， 标题，内容主体，状态，作者，日期，创建时间，修改时间，播放量，rank值，是否允许评论，打赏
    // 保留草稿，可能会有许多冗余信息，所以新增草稿审计，版本号，如果需要发布，则将草稿内容同步到conten表，
    // 草稿表 外键 content表的id，
    // 附加信息，异步加载？ 评论 等

    private Article article;
    private List<Long> tagIds;
    private List<Long> categoryIds;
}
