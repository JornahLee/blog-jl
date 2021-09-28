/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * DateTime: 2018/7/24 23:03
 **/
package com.jornah.model.newP;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 文章关联信息表
 */
@Data
@SuperBuilder
public class ArticleCategoryMap extends BaseEntity implements Serializable {

    /**
     * 文章主键
     */
    private Long arId;
    /**
     * 分类主键
     */
    private Long cateId;
}
