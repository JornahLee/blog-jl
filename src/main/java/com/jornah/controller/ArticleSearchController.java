package com.jornah.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jornah.controller.BaseController;
import com.jornah.model.dto.ArticleHitInfo;
import com.jornah.model.dto.ArticleSearchRequest;
import com.jornah.model.dto.SearchResult;
import com.jornah.service.es.EsContentService;
import com.jornah.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/blog/article")
@RestController
@CrossOrigin
@Api("文章搜索")
public class ArticleSearchController extends BaseController {

    @Autowired
    private EsContentService esContentService;

    @PostMapping("/search")
    @ApiOperation("v1 search")
    public APIResponse<List<SearchResult>> search(ArticleSearchRequest request) {
        List<SearchResult> res = doSearch(request);
        return APIResponse.success(res);
    }
    @PostMapping("/v2/search")
    @ApiOperation("v2 search")
    public APIResponse<List<ArticleHitInfo>> searchWithAnchor(@RequestBody ArticleSearchRequest request) {
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
        return res;
    }
    private List<ArticleHitInfo> doV2Search(ArticleSearchRequest request) {
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        String inputStr = request.getSearchStr();
        int pageNum = request.getPageNum() <= 0 ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() <= 0 ? 5 : request.getPageSize();
        List<ArticleHitInfo> withAnchorByContentOrTitle = esContentService.findWithAnchorByContentOrTitle(inputStr, pageNum, pageSize);
        return withAnchorByContentOrTitle;
    }

}
