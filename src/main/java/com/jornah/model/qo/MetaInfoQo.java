package com.jornah.model.qo;

import lombok.Data;

import java.util.List;

/**
 * @author licong
 * @date 2021/10/8 11:32
 */
@Data
public class MetaInfoQo {
    private Long articleId;
    private Long cateId;
    private List<Long> tagIds;
}
