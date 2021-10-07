/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/8/3 16:31
 **/
package com.jornah.model.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class AttachDto  {
    /**
     * 主键编号
     */
    private Long id;

    /**
     * 文件名称
     */
    private String fname;

    /**
     * 文件类型
     */
    private String ftype;

    /**
     * 文件的地址
     */
    private String fkey;

    /**
     * 上传人的ID
     */
    private Integer authorId;

    /**
     * 创建的时间戳
     */
    private Instant created;

}
