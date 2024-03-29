package com.jornah.service.es;

import com.google.common.collect.Lists;
import com.jornah.dao.ArticleDao;
import com.jornah.model.entity.Article;
import com.jornah.model.dto.ArticleBodyHitInfo;
import com.jornah.model.dto.ArticleHitInfo;
import com.jornah.model.dto.ContentEsDTO;
import com.jornah.model.dto.SearchResult;
import com.jornah.utils.MyStringUtil;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jornah.utils.MyStringUtil.LineNoFormat;
import static com.jornah.utils.MyStringUtil.LineNoRegex;
import static com.jornah.utils.MyStringUtil.generateLineNumberForText;
import static java.lang.Integer.parseInt;

@Service
public class EsContentService {
    private final ElasticsearchRestTemplate template;

    public static final String LINE_NO_REGEX = "(?<=:)\\d+(?=:)";
    private static final Pattern lineNoPattern = Pattern.compile(LINE_NO_REGEX);
    private static final Pattern headerPattern = Pattern.compile("(" + LineNoRegex + ")( *#|#).+");

    @Autowired
    public EsContentService(ElasticsearchRestTemplate template) {
        this.template = template;
    }

    @Autowired
    private ArticleDao articleDao;

    public List<SearchResult> findByContentOrTitle(String input, int pageNum, int pageSize) {
        int pageIndex = pageNum - 1;
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize);

        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery(input, "content", "title"))
                .withPageable(pageRequest)
                .withFields("content", "title", "url")
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
                .withFields("content", "title", "url")
                .withHighlightBuilder(new HighlightBuilder().field("content").field("title").tagsSchema("styled"))
                .build();
        List<ArticleHitInfo> resultList = Lists.newArrayList();

        SearchHits<ContentEsDTO> search = template.search(query, ContentEsDTO.class);
        search.stream().forEach(hit -> {
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

        // 假如这个header他没有LineNo呢，程序应该兼容，
        //
        HashMap<Integer, String> headWithLineNo = new HashMap<>();
        for (String headLine : headLines) {
            Matcher matcher = lineNoPattern.matcher(headLine);
            if (matcher.find()) {
                int lineNo = parseInt(matcher.group());
                headWithLineNo.put(lineNo, headLine);
            }
        }
        return headWithLineNo;
    }

    public void exportDataToEs() {
        articleDao.findAll().forEach(content -> {
            ContentEsDTO contentEsDTO = new ContentEsDTO(content.getId().toString(), "/detail/" + content.getId(), content.getTitle(),
                    content.getCreated().toEpochMilli(), content.getUpdated().toEpochMilli(),
                    MyStringUtil.generateLineNumberForText(content.getContent(), MyStringUtil.LineNoFormat, true));
            template.save(contentEsDTO);
        });
    }

    @Async
    public void add(Article article) {
        article.setContent(generateLineNumberForText(article.getContent(), LineNoFormat, true));
        ContentEsDTO contentEsDTO = new ContentEsDTO(article.getId().toString(), "/detail/" + article.getId(), article.getTitle(),
                article.getCreated().toEpochMilli(), article.getUpdated().toEpochMilli(),
                article.getContent());
        template.save(contentEsDTO);
    }

    @Async
    public void update(Article article) {
        // 更新有多种方式 https://www.jianshu.com/p/1636ff0b800d
        article.setContent(generateLineNumberForText(article.getContent(), LineNoFormat, true));
        if (template.exists(article.getId().toString(), ContentEsDTO.class)) {
            UpdateQuery updateQuery = UpdateQuery.builder(article.getId().toString()).withDocument(Document.create().append("content", article.getContent()))
                    .build();
            template.update(updateQuery, IndexCoordinates.of(ContentEsDTO.INDEX_NAME));
        } else {
            add(article);
        }
    }

    @Async
    public void delete(String id) {
        template.delete(id, IndexCoordinates.of(ContentEsDTO.INDEX_NAME));
    }

    private List<ArticleBodyHitInfo> generateBodyHitInfo(List<String> hitsContext, Map<Integer, String> headLines) {
        if (Objects.isNull(hitsContext) || Objects.isNull(headLines)) {
            return null;
        }
        Map<Integer, String> hitWithLineNo = new HashMap<>();
        for (String ctx : hitsContext) {
            Matcher matcher = lineNoPattern.matcher(ctx);
            List<Integer> matches = new ArrayList<>();
            while (matcher.find()) {
                matches.add(parseInt(matcher.group()));
            }
            if (matches.size() > 0) {
                hitWithLineNo.put(matches.get(matches.size() - 1), ctx);
            }
        }
        return hitWithLineNo.entrySet().stream().map(
                entry -> {
                    ArticleBodyHitInfo info = new ArticleBodyHitInfo();
                    Integer lineNo = entry.getKey();
                    // 默认升序排列,第一个差值最小
                    Stream<Integer> sorted = headLines.keySet().stream()
                            .filter(key -> key <= lineNo)
                            .sorted(Comparator.comparingInt(key -> lineNo - key));
                    Integer underHeadLineKey = sorted.findFirst().orElse(0);
                    String headLineStr = Optional.ofNullable(headLines.get(underHeadLineKey)).orElse("");
                    info.setUnderHeadOriginal(headLineStr);
                    // 因为es命中上下文，可能存在 行号标记不全的情况，如 :1 或者 1:
                    String regex = LineNoRegex + "|:\\d+|\\d+:";
                    info.setHitContext(Optional.ofNullable(entry.getValue()).map(val -> val.replaceAll(regex, "")).orElse(""));
                    info.setUnderHead(headLineStr.replaceAll("#|" + LineNoRegex, "").trim().replaceAll("\\s", "-"));
                    return info;
                }
        ).collect(Collectors.toList());
    }
}
