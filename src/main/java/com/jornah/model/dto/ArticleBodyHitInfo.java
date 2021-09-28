package com.jornah.model.dto;

public class ArticleBodyHitInfo {
    private String hitContext;
    private String underHeadOriginal;
    private String underHead;

    public ArticleBodyHitInfo() {
    }

    public ArticleBodyHitInfo(String hitContext, String underHeadOriginal, String underHead) {
        this.hitContext = hitContext;
        this.underHeadOriginal = underHeadOriginal;
        this.underHead = underHead;
    }

    public String getHitContext() {
        return hitContext;
    }

    public void setHitContext(String hitContext) {
        this.hitContext = hitContext;
    }

    public String getUnderHeadOriginal() {
        return underHeadOriginal;
    }

    public void setUnderHeadOriginal(String underHeadOriginal) {
        this.underHeadOriginal = underHeadOriginal;
    }

    public String getUnderHead() {
        return underHead;
    }

    public void setUnderHead(String underHead) {
        this.underHead = underHead;
    }

    @Override
    public String toString() {
        return "ArticleBodyHitInfo{" +
                "contextWithHit='" + hitContext + '\'' +
                ", underHeadOriginal='" + underHeadOriginal + '\'' +
                ", underHead='" + underHead + '\'' +
                '}';
    }
}
