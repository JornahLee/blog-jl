package com.jornah.model.dto;

import java.util.List;

public class ArticleHitInfo {
    private String url;
    private String title;
    private List<ArticleBodyHitInfo> bodyHitInfoList;

    public ArticleHitInfo() {
    }

    public ArticleHitInfo(String url, String title, List<ArticleBodyHitInfo> bodyHitInfoList) {
        this.url = url;
        this.title = title;
        this.bodyHitInfoList = bodyHitInfoList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ArticleBodyHitInfo> getBodyHitInfoList() {
        return bodyHitInfoList;
    }

    public void setBodyHitInfoList(List<ArticleBodyHitInfo> bodyHitInfoList) {
        this.bodyHitInfoList = bodyHitInfoList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
