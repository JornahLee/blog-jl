/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/26 15:15
 **/
package com.wip.model.dto.cond;

import cn.hutool.core.util.ReflectUtil;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * 文章查询条件
 */
public class ContentCond {
    public static final String PUBLISH = "publish";
    /**
     * 标签
     */
    private String tag;
    /**
     * 类别
     */
    private String category;
    /**
     * 状态
     */
    private String status;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容匹配
     */
    private String content;
    /**
     * 文章类型
     */
    private String type;
    /**
     * 开始时间戳
     */
    private Integer startTime;
    /**
     * 结束时间戳
     */
    private Integer endTime;

    /**
     * 排序的SQL语句
     */
    private String howToOrder;

    public void setValuesFrom(Map<String, String> source) {
        Field[] fields = ReflectUtil.getFields(this.getClass());
        source.forEach(
                (key, value) -> Arrays.stream(fields).filter(field -> field.getName().contains(key) && !value.contains("所有分类"))
                        .findFirst().ifPresent(field -> {
                            try {
                                field.setAccessible(true);
                                field.set(this, value);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }));
    }

    public String getHowToOrder() {
        return howToOrder;
    }

    public void setHowToOrder(String howToOrder) {
        this.howToOrder = howToOrder;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "ContentCond{" +
                "tag='" + tag + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", howToOrder='" + howToOrder + '\'' +
                '}';
    }
    public String getMd5(){
        return Md5Crypt.apr1Crypt(this.toString(),"1");
    }
}
