package com.wip.model.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.time.Instant;

@Document(indexName = "content")
public class ContentEsDTO {
    /**
     * 文章的主键编号
     */
    @Id
    private String id;
    /**
     * 内容标题
     */
    @Field(searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private String title;
    /**
     * 内容生成时的GMT unix时间戳
     */
    private Instant created;
    /**
     * 内容更改时的GMT unix时间戳
     */
    private Instant modified;
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

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
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
}
