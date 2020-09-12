/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/24 16:59
 **/
package com.wip.model.dto.cond;

/**
 * Meta查询条件
 */
public class MetaCond {

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    public static MetaCond of(String name,String type){
        MetaCond cond=new MetaCond();
        cond.setType(type);
        cond.setName(name);
        return cond;
    }
    public static MetaCond of(String type){
        MetaCond cond=new MetaCond();
        cond.setType(type);
        return cond;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
