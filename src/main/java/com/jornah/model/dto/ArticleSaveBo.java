package com.jornah.model.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ArticleSaveBo {

    @NotNull
    private Long id;
    /**
     * 内容标题
     */
    @NotBlank
    private String title;
    /**
     * 标题图片
     */
    private String titlePic;
    /**
     * 内容文字
     */
    @NotBlank
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
    /**
     * 推荐级别
     */
    private Integer recommendLevel;

}
