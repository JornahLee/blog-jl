package com.jornah.model.qo;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

/**
 * @author licong
 * @date 2021/9/25 13:20
 */
@Data
@Builder
public class ArticleQo {
    private int pageSize;
    private int pageNum;
    private String sortField;
    private String sort;
    public boolean isAsc(){
        return Objects.equals("asc", sort);
    }
}
