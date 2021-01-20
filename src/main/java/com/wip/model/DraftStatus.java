package com.wip.model;

import org.apache.commons.lang3.StringUtils;

public class DraftStatus {
    public static final int DRAFT = 0;
    public static final int PUBLISHED = 0;

    public static int getByString(String statusStr) {
        statusStr = StringUtils.defaultString(statusStr);
        switch (statusStr) {
            case "draft":
                return DRAFT;
            case "publish":
                return PUBLISHED;
            default:
                return -1;
        }
    }
}
