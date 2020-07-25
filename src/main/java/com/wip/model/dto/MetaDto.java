/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/24 16:59
 **/
package com.wip.model.dto;

import com.wip.model.Meta;

/**
 * 标签、分类列表
 */
public class MetaDto extends Meta {

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
