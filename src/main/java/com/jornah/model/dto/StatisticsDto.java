/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/8/2 9:25
 **/
package com.jornah.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 后台统计对象
 */
@Data
public class StatisticsDto implements Serializable {

    /**
     * 文章数
     */
    private Long articles;

    /**
     * 评论数
     */
    private Long comments;

    /**
     * 链接数
     */
    private Long links;

    /**
     * 文件数
     */
    private Long attachs;

}
