package com.wip.service.es;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wip.dao.ContentDao;
import com.wip.model.Content;
import com.wip.model.dto.ContentEsDTO;
import com.wip.model.dto.SearchResult;
import com.wip.utils.GsonUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EsContentService {
    private ElasticsearchRestTemplate template;

    @Autowired
    public EsContentService(ElasticsearchRestTemplate template) {
        this.template = template;
    }

    @Autowired
    private ContentDao contentDao;

    public List<SearchResult> findByContentOrTitle(String input, int pageNum, int pageSize) {
        int pageIndex = pageNum - 1;
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize);

        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery(input, "content", "title"))
                .withPageable(pageRequest)
                .withHighlightBuilder(new HighlightBuilder().field("content").field("title").tagsSchema("styled"))
                .build();
        List<SearchResult> resultList = Lists.newArrayList();
        template.search(query, ContentEsDTO.class).stream()
                .forEach(hit -> resultList.add(new SearchResult(hit.getContent().getUrl(), hit.getContent().getTitle(), hit.getHighlightFields())));
        return resultList;
    }

    public void exportDataToEs() {
        contentDao.findAll().forEach(content -> {
            ContentEsDTO contentEsDTO = new ContentEsDTO(content.getCid().toString(), "/detail/" + content.getCid(), content.getTitle(),
                    content.getCreated().toEpochMilli(), content.getModified().toEpochMilli(),
                    content.getContent());
            template.save(contentEsDTO);
        });
        // contentDao.findAll().forEach();
    }

    @Async
    public void add(Content content) {
        ContentEsDTO contentEsDTO = new ContentEsDTO(content.getCid().toString(), "/detail/" + content.getCid(), content.getTitle(),
                content.getCreated().toEpochMilli(), content.getModified().toEpochMilli(),
                content.getContent());
        template.save(contentEsDTO);
    }

    @Async
    public void update(Content content) {
        // 更新有多种方式 https://www.jianshu.com/p/1636ff0b800d
        if (template.exists(content.getCid().toString(), ContentEsDTO.class)) {
            UpdateQuery updateQuery = UpdateQuery.builder(content.getCid().toString()).withDocument(Document.create().append("content", content.getContent()))
                    .build();
            template.update(updateQuery, IndexCoordinates.of(ContentEsDTO.INDEX_NAME));
        } else {
            add(content);
        }
    }

    @Async
    public void delete(String id) {
        template.delete(id, IndexCoordinates.of(ContentEsDTO.INDEX_NAME));
    }


}
