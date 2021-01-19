package com.wip.model;

public class Draft extends BaseEntity{
    private long contentId;
    private String diffText;
    // id 就可以充当版本号的作用
    // private int versionNumber;

    public Draft(){}
    public Draft(long contentId, String diffText) {
        this.contentId = contentId;
        this.diffText = diffText;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getDiffText() {
        return diffText;
    }

    public void setDiffText(String diffText) {
        this.diffText = diffText;
    }


}
