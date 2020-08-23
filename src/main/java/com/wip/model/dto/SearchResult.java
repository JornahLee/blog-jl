package com.wip.model.dto;


import java.util.List;
import java.util.Map;

public class SearchResult {
    private String url;
    private String articleName;

    private Map<String, List<String>> hitsWithHighLight;

    public SearchResult(String url, String articleName, Map<String, List<String>> hitsWithHighLight) {
        this.url = url;
        this.articleName = articleName;
        this.hitsWithHighLight = hitsWithHighLight;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, List<String>> getHitsWithHighLight() {
        return hitsWithHighLight;
    }

    public void setHitsWithHighLight(Map<String, List<String>> hitsWithHighLight) {
        this.hitsWithHighLight = hitsWithHighLight;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }
}
