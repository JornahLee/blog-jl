package com.wip.service.es;

import com.wip.model.Content;
import com.wip.model.dto.ContentEsDTO;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EsContentService {
    private ElasticsearchRestTemplate template;

    @Autowired
    public EsContentService(ElasticsearchRestTemplate template) {
        this.template = template;
    }

    public List<ContentEsDTO> findByContentOrTitle(String input) {
        PageRequest pageRequest = PageRequest.of(0, 3);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery(input,"content","title")).withPageable(pageRequest).build();
        SearchHits<ContentEsDTO> searchHits = template.search(query, ContentEsDTO.class);
        List<ContentEsDTO> resList=new ArrayList<>();
        searchHits.forEach(res->resList.add(res.getContent()));
        return resList;
    }
}
