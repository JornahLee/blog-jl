package com.wip.controller.v2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wip.controller.BaseController;
import com.wip.model.dto.ArticleHitInfo;
import com.wip.model.dto.ArticleSearchRequest;
import com.wip.model.dto.ContentEsDTO;
import com.wip.model.dto.SearchResult;
import com.wip.service.article.ContentService;
import com.wip.service.es.EsContentService;
import com.wip.utils.APIResponse;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/article")
@RestController
public class ArticleSearchController extends BaseController {

    @Autowired
    private EsContentService esContentService;

    @RequestMapping("/search")
    public APIResponse<List<SearchResult>> search(ArticleSearchRequest request) {
        List<SearchResult> res = doSearch(request);
        return APIResponse.success(res);
    }
    @RequestMapping("/v2/search")
    public APIResponse<List<ArticleHitInfo>> searchWithAnchor(ArticleSearchRequest request) {
        List<ArticleHitInfo> res = doV2Search(request);
        return APIResponse.success(res);
    }

    // 如果有必要 要抽取到Service层中
    private List<SearchResult> doSearch(ArticleSearchRequest request) {
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        String inputStr = request.getSearchStr();
        int pageNum = request.getPageNum() <= 0 ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() <= 0 ? 5 : request.getPageSize();
        List<SearchResult> res = esContentService.findByContentOrTitle(inputStr, pageNum, pageSize);
        System.out.println("--licg---     gson.toJson(APIResponse.success(res)) : " + gson.toJson(APIResponse.success(res)) + "    -----");
        return res;
    }
    private List<ArticleHitInfo> doV2Search(ArticleSearchRequest request) {
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        String inputStr = request.getSearchStr();
        int pageNum = request.getPageNum() <= 0 ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() <= 0 ? 5 : request.getPageSize();
        List<ArticleHitInfo> withAnchorByContentOrTitle = esContentService.findWithAnchorByContentOrTitle(inputStr, pageNum, pageSize);
        System.out.println("--licg---     gson.toJson(APIResponse.success(res)) : " + gson.toJson(APIResponse.success(withAnchorByContentOrTitle)) + "    -----");
        return withAnchorByContentOrTitle;
    }

}
