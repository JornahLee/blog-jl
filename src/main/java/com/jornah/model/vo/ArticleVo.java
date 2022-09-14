package com.jornah.model.vo;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
public class ArticleVo {
    private Long id;
    private Instant created;
    private Instant updated;
    /**
     * 内容标题
     */
    private String title;
    /**
     * 标题图片
     */
    private String titlePic;
    /**
     * 内容缩略名
     */
    private String slug;
    /**
     * 内容文字
     */
    private String content;

    /**
     * 内容所属用户id
     */
    private Long authorId;
    /**
     * 内容状态
     */
    private String status;
    /**
     * 点击次数
     */
    private Integer hits;
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

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 文章类型
     */
    private String type;
    /**
     * 是否加密content
     */
    private boolean encryptEnable;

    /**
     * 解密是否成功
     */
    private boolean decryptSuccess;

}
