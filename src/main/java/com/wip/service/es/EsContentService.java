package com.wip.service.es;

import com.google.common.collect.Lists;
import com.wip.dao.ContentDao;
import com.wip.model.Content;
import com.wip.model.dto.ArticleBodyHitInfo;
import com.wip.model.dto.ArticleHitInfo;
import com.wip.model.dto.ContentEsDTO;
import com.wip.model.dto.SearchResult;
import com.wip.utils.MyStringUtil;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wip.utils.MyStringUtil.LineNoRegex;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toMap;

@Service
public class EsContentService {
    private ElasticsearchRestTemplate template;

    private static Pattern lineNoPattern = Pattern.compile("(?<=::L-)\\d+(?=::)");
    private static Pattern headerPattern = Pattern.compile("(" + LineNoRegex + ")( *#|#).+");

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
            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            SearchResult searchResult = new SearchResult(url, title, highlightFields);
            resultList.add(searchResult);

        });
        return resultList;
    }

    public List<ArticleHitInfo> findWithAnchorByContentOrTitle(String input, int pageNum, int pageSize) {
        int pageIndex = pageNum - 1;
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize);

        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery(input, "content", "title"))
                .withPageable(pageRequest)
                .withHighlightBuilder(new HighlightBuilder().field("content").field("title").tagsSchema("styled"))
                .build();
        List<ArticleHitInfo> resultList = Lists.newArrayList();

        template.search(query, ContentEsDTO.class).stream().forEach(hit -> {
            String url = hit.getContent().getUrl();
            String title = hit.getContent().getTitle();
            String content = hit.getContent().getContent();
            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            // get all headline and no
            Map<Integer, String> headLines = getHeadFromContent(content);
            List<ArticleBodyHitInfo> articleBodyHitInfos = generateBodyHitInfo(highlightFields.get("content"), headLines);
            ArticleHitInfo articleHitInfo = new ArticleHitInfo(url, title, articleBodyHitInfos);
            resultList.add(articleHitInfo);

        });
        return resultList;
    }

    /**
     * @return mam.key=标题的行号,value=标题的内容
     */
    private Map<Integer, String> getHeadFromContent(String content) {
        List<String> headLines = new ArrayList<>();
        Optional.ofNullable(content).ifPresent(c -> {
            Matcher matcher = headerPattern.matcher(c);
            while (matcher.find()) {
                headLines.add(matcher.group());
            }
        });
        return headLines.stream()
                .collect(toMap(
                        str -> {
                            Matcher matcher = lineNoPattern.matcher(str);
                            if (matcher.find()) {
                                return parseInt(matcher.group());
                            } else {
                                throw new RuntimeException("content has no lineno str");
                            }
                        },
                        str -> str
                ));
    }

    public void exportDataToEs() {
        contentDao.findAll().forEach(content -> {
            ContentEsDTO contentEsDTO = new ContentEsDTO(content.getCid().toString(), "/detail/" + content.getCid(), content.getTitle(),
                    content.getCreated().toEpochMilli(), content.getModified().toEpochMilli(),
                    MyStringUtil.generateLineNumberForText(content.getContent(), MyStringUtil.LineNoFormat, true));
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
                            List<Integer> matches = new ArrayList<>();
                            while (matcher.find()) {
                                matches.add(parseInt(matcher.group()));
                            }
                            if (matches.size() > 1) {
                                return matches.get(matches.size() - 1);
                            } else {
                                throw new RuntimeException("content has no lineNo str");
                            }
                        },
                        ctx -> ctx
                ));
        return hitWithLineNo.entrySet().stream().map(
                entry -> {
                    ArticleBodyHitInfo info = new ArticleBodyHitInfo();
                    Integer lineNo = entry.getKey();
                    // 默认升序排列,第一个差值最小
                    Stream<Integer> sorted = headLines.keySet().stream()
                            .filter(key -> key <= lineNo)
                            .sorted(Comparator.comparingInt(key -> lineNo - key));
                    Integer underHeadLineKey = sorted.findFirst().orElseThrow(() -> new RuntimeException(""));
                    String headLineStr = headLines.get(underHeadLineKey);
                    info.setUnderHeadOriginal(headLineStr);
                    info.setHitContext(Optional.ofNullable(entry.getValue()).map(val -> val.replaceAll(LineNoRegex, "")).orElse(""));
                    info.setUnderHead(headLineStr.trim().replaceAll("#|" + LineNoRegex, ""));
                    return info;
                }
        ).collect(Collectors.toList());
    }
}
