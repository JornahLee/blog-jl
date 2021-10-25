/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/23 17:00
 **/
package com.jornah.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * 日志表
 */
@Data
public class Log extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产生的动作
     */
    private String action;

    /**
     * 产生的数据
     */
    private String data;

    /**
     * 发生人id
     */
    private Long authorId;

    /**
     * 日志产生的IP
     */
    private String ip;

}
