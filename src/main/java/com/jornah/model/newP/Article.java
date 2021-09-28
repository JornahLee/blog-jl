/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/25 16:30
 **/
package com.jornah.model.newP;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 文章表
 */
@Data
@SuperBuilder
@NoArgsConstructor
@TableName("article")
public class Article extends  BaseEntity implements Serializable {
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
     * 内容类别
     */
    private String type;
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


}
