package com.jornah.model.entity;

import com.jornah.model.entity.BaseEntity;

public class Draft extends BaseEntity {
    private Long contentId;
    private String diffText;
    private Integer status;
    // id 就可以充当版本号的作用
    // private int versionNumber;

    public Draft(){}
    public Draft(long contentId, String diffText ,Integer status) {
        this.contentId = contentId;
        this.diffText = diffText;
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getDiffText() {
        return diffText;
    }

    public void setDiffText(String diffText) {
        this.diffText = diffText;
    }


}
