package com.jornah.dao.es;
//
// import com.wip.model.Content;
// import io.swagger.models.auth.In;
// import org.springframework.data.repository.CrudRepository;
// import org.springframework.stereotype.Repository;
//
import com.jornah.model.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Repository
public class ContentRepositoryImpl implements ContentRepository{
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public List<Article> findByContentOrTitle(String content, String title){
        // SearchQuery searchQuery =
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("name", "gaolujie"))
                .withPageable(PageRequest.of(1, 1)).build();//分页

        SearchHits<Article> hits = elasticsearchTemplate.search(searchQuery, Article.class);
        return hits.toList().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }
}
