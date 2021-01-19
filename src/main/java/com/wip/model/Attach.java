/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/8/3 16:18
 **/
package com.wip.model;

import java.io.Serializable;
import java.time.Instant;

/**
 * 网站图片文件相关表
 */
public class Attach implements Serializable {

    /**
     * 主键编号
     */
    private Integer id;

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

    /**
     * 创建的时间戳
     */
    private Instant created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getFkey() {
        return fkey;
    }

    public void setFkey(String fkey) {
        this.fkey = fkey;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}
