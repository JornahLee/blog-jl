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
public class ArticleTagMap extends BaseEntity implements Serializable {

    /**
     * 文章主键
     */
    private Long arId;
    /**
     * 标签主键
     */
    private Long tagId;

}
