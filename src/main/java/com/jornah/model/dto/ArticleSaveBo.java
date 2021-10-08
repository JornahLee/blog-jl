package com.jornah.model.dto;

import lombok.Data;

@Data
public class ArticleSaveBo {
    //todo 一篇文章 可以有多个标签 多个分类，所以标签和分类重复了
    // 需要的信息 id ， 标题，内容主体，状态，作者，日期，创建时间，修改时间，播放量，rank值，是否允许评论，打赏
    // 保留草稿，可能会有许多冗余信息，所以新增草稿审计，版本号，如果需要发布，则将草稿内容同步到conten表，
    // 草稿表 外键 content表的id，
    // 附加信息，异步加载？ 评论 等


    private Long id;
    /**
     * 内容标题
     */
    private String title;
    /**
     * 标题图片
     */
    private String titlePic;
    /**
     * 内容文字
     */
    private String content;

    /**
     * 内容状态
     */
    private String status;
    /**
     * 内容所属评论数
     */
    private Integer commentsNum;
    /**
     * 是否允许评论
     */
    private Boolean allowComment;
    /**
     * 排序权重 现在默认为10
     */
    private Integer orderWeight;
    /**
     * 允许出现打赏
     */
    private Boolean allowFeed;

}
