package com.wip.service.es;

import com.google.common.collect.Lists;
import com.wip.dao.ContentDao;
import com.wip.model.Content;
import com.wip.model.dto.ContentEsDTO;
import com.wip.model.dto.SearchResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
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
                .forEach(hit -> resultList.add(new SearchResult(hit.getContent().getUrl(),hit.getContent().getTitle(), hit.getHighlightFields())));
        return resultList;
    }
    public void exportDataToEs(){
        List<ContentEsDTO> list=new ArrayList<>(10);
        contentDao.findAll().forEach(content -> {
                    ContentEsDTO contentEsDTO = new ContentEsDTO("/detail/"+content.getCid(), content.getTitle(),
                            content.getCreated().toEpochMilli(), content.getModified().toEpochMilli(),
                            content.getContent());
                    template.save(contentEsDTO);
                });
        // contentDao.findAll().forEach();
    }
    public void add(ContentEsDTO contentEsDTO) {
        template.save(contentEsDTO);
    }

}
