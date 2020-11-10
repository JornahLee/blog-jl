package com.wip.service.es;

import com.google.common.collect.Lists;
import com.wip.dao.ContentDao;
import com.wip.model.Content;
import com.wip.model.dto.ArticleBodyHitInfo;
import com.wip.model.dto.ContentEsDTO;
import com.wip.model.dto.SearchResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toMap;

@Service
public class EsContentService {
    private ElasticsearchRestTemplate template;

    private static Pattern lineNoPattern = Pattern.compile("(?<=::L-)\\d+(?=::)");

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

        template.search(query, ContentEsDTO.class).stream().forEach(hit -> {
            String url = hit.getContent().getUrl();
            String title = hit.getContent().getTitle();
            String content = hit.getContent().getContent();
            // get all headline and no
            //todo content新增表 用于保存目录，先暂时使用生成的方案
            Map<Integer, String> headLines = getHeadFromContent(content);

            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            SearchResult searchResult = new SearchResult(url, title, highlightFields);
            resultList.add(searchResult);

        });
        return resultList;
    }

    /**
     * @return mam.key=标题的行号,value=标题的内容
     */
    private Map<Integer, String> getHeadFromContent(String content) {
        List<String> headLines = new ArrayList<>();
        Optional.ofNullable(content).ifPresent(c -> {
            Matcher matcher = Pattern.compile("( *#|#).+").matcher(c);
            while (matcher.find()) {
                headLines.add(matcher.group());
            }
        });
        return headLines.stream()
                .collect(toMap(
                        str -> parseInt(lineNoPattern.matcher(str).group()),
                        str -> str
                ));

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


    private List<ArticleBodyHitInfo> generateBodyHitInfo(List<String> hitsContext, Map<Integer, String> headLines) {
        Map<Integer, String> hitWithLineNo = hitsContext.stream()
                .collect(toMap(
                        ctx -> {
                            Matcher matcher = lineNoPattern.matcher(ctx);
                            String lineNoStr = "";
                            if (matcher.find()) {
                                lineNoStr = matcher.group();
                            }
                            return parseInt(lineNoStr);
                        },
                        ctx -> ctx
                ));

        List<ArticleBodyHitInfo> hitInfos = hitWithLineNo.entrySet().stream().map(
                entry -> {
                    ArticleBodyHitInfo info = new ArticleBodyHitInfo();
                    Integer lineNo = entry.getKey();
                    // 默认升序排列,第一个差值最小
                    Stream<Integer> sorted = headLines.keySet().stream()
                            .filter(key -> key > lineNo)
                            .sorted(Comparator.comparingInt(key -> key - lineNo));
                    Integer integer = sorted.findFirst().orElse(1);
                    String headLineStr = headLines.get(integer);
                    info.setUnderHead(headLineStr);
                    info.setContextWithHit(entry.getValue());
                    return info;
                }
        ).collect(Collectors.toList());
        return hitInfos;
    }
}
