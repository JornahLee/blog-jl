package com.wip.model.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Document(indexName = "content")
public class ContentEsDTO {
    /**
     * 文章的主键编号
     */
    @Id
    private String id;

    private String url;
    /**
     * 内容标题
     */
    @Field(searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private String title;
    /**
     * 内容生成时的GMT unix时间戳
     */
    @Field(type = FieldType.Date,format = DateFormat.basic_date_time)
    private Long created;
    /**
     * 内容更改时的GMT unix时间戳
     */
    @Field(type = FieldType.Date,format = DateFormat.basic_date_time)
    private Long modified;
    /**
     * 内容文字
     */
    @Field(searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private String content;
    /**
     * 内容类别
     */
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getModified() {
        return modified;
    }

    public void setModified(Long modified) {
        this.modified = modified;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ContentEsDTO{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
