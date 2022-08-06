package com.jornah.model.enums;

/**
 * @author licong
 * @date 2022/8/3 10:30
 */
public enum ArticleStatus {
    DELETED("DELETED"), PUBLISHED("publish"), DRAFT("draft");
    private final String value;

    ArticleStatus(String val) {
        this.value = val;
    }

    public String getValue() {
        return value;
    }
}
