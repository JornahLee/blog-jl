package com.wip.model.dto;

public class ArticleBodyHitInfo {
    private String contextWithHit;
    private String underHead;

    public ArticleBodyHitInfo() {
    }

    public ArticleBodyHitInfo(String contextWithHit, String underHead) {
        this.contextWithHit = contextWithHit;
        this.underHead = underHead;
    }

    public String getContextWithHit() {
        return contextWithHit;
    }

    public void setContextWithHit(String contextWithHit) {
        this.contextWithHit = contextWithHit;
    }

    public String getUnderHead() {
        return underHead;
    }

    public void setUnderHead(String underHead) {
        this.underHead = underHead;
    }
}
