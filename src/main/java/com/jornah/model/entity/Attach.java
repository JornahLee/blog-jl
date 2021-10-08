/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/8/3 16:18
 **/
package com.jornah.model.entity;

import com.jornah.model.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 网站图片文件相关表
 */
@Data
public class Attach extends BaseEntity implements Serializable {

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
     * 上传人的ID
     */
    private String md5;


}
