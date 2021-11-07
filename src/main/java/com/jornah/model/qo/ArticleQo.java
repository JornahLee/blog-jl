package com.jornah.model.qo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * @author licong
 * @date 2021/9/25 13:20
 */
@Data
@Builder
public class ArticleQo {
    @NotNull
    private Map<String,Long> queryKeyColumns;
    @Min(5)
    private int pageSize;
    @Min(1)
    private int pageNum;
    private String sortField;
    private String sort;
    public boolean isAsc(){
        return Objects.equals("asc", sort);
    }
}
