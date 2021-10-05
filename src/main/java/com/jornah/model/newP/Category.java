/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * DateTime: 2018/7/24 23:03
 **/
package com.jornah.model.newP;

import lombok.Data;

import java.io.Serializable;

/**
 * 文章关联信息表
 */
@Data
public class Category extends BaseEntity implements Serializable {

    private String name;

}
