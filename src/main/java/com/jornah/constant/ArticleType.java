package com.jornah.constant;

/**
 * @author licong
 * @date 2022/8/16 10:22
 */
public enum ArticleType {
    /**
     * 随笔
     */
    ESSAY,
    /**
     * 日记
     */
    DIARY,
    /**
     * 转载
     */
    REPRINTED;

    public boolean equalTo(String type) {
        return this.name().equalsIgnoreCase(type);
    }
}
