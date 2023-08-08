package com.jornah.model.qo;

import lombok.Data;

import java.util.List;

/**
 * @author licong
 * @date 2023/8/7 00:17
 */
@Data
public class ArticleBatchQo {
    private List<Long> articleIdList;
}
