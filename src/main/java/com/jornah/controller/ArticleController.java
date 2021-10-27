package com.jornah.controller;


import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.jornah.anno.AccessControl;
import com.jornah.cache.CacheService;
import com.jornah.model.dto.ArticleSaveBo;
import com.jornah.model.entity.Article;
import com.jornah.model.DraftStatus;
import com.jornah.model.qo.ArticleQo;
import com.jornah.model.vo.ArticleMetaInfo;
import com.jornah.model.vo.ArticleVo;
import com.jornah.service.DraftService;
import com.jornah.service.article.ArticleService;
import com.jornah.utils.APIResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

import static java.time.Instant.now;

@RestController()
@RequestMapping("/blog/article")
@CrossOrigin
public class ArticleController extends BaseController{

    @Autowired
    private ArticleService articleService;
    @Autowired
    private DraftService draftService;
    @Autowired
    private CacheService cacheService;

    @ApiOperation("保存或更新")
    @PostMapping(value = "/saveOrUpdate")
    @AccessControl
    public APIResponse<?> saveOrUpdate(@RequestBody ArticleSaveBo articleSaveBo) {
        long ret = articleService.saveOrUpdate(articleSaveBo);
        return APIResponse.success(ImmutableMap.of("id", ret));
    }

    @ApiOperation("查询单个文档")
    @GetMapping(value = "/{id}")
    public APIResponse<ArticleVo> getArticle(@PathVariable Long id) {
        ArticleVo articleVo = articleService.getArticleBy(id);
        return APIResponse.success(articleVo);
    }

    @ApiOperation("分页查询文档")
    @PostMapping(value = "/list")
    public APIResponse<PageInfo<ArticleVo>> getArticleList(@RequestBody ArticleQo qo) {
        PageInfo<ArticleVo> orderBy = articleService.getArticlesOrderBy(qo);
        return APIResponse.success(orderBy);
    }

    @ApiOperation("按标签 分页查询文档")
    @PostMapping(value = "/list/byTag")
    public APIResponse<PageInfo<ArticleVo>> getArticleListByTag(@RequestBody ArticleQo qo) {
        Long tagId = qo.getQueryKeyColumns().get("byTag");
        PageInfo<ArticleVo> orderBy = articleService.getArticleByTag(tagId, qo.getPageNum(), qo.getPageSize());
        Gson gson = new Gson();
        cacheService.setValue("ariticle:list",gson.toJson(orderBy.getList()));
        return APIResponse.success(orderBy);
    }

    @ApiOperation("按分类 分页查询文档")
    @PostMapping(value = "/list/byCate")
    public APIResponse<PageInfo<ArticleVo>> getArticleListByCate(@RequestBody ArticleQo qo) {
        Long cateId = qo.getQueryKeyColumns().get("byCate");
        PageInfo<ArticleVo> orderBy = articleService.getArticleByCate(cateId, qo.getPageNum(), qo.getPageSize());
        Gson gson = new Gson();
        cacheService.setValue("ariticle:list",gson.toJson(orderBy.getList()));
        return APIResponse.success(orderBy);
    }

    @ApiOperation("保存草稿")
    @PostMapping(value = "/draft/save")
    @AccessControl
    public APIResponse<?> saveDraft(Article article) {
        draftService.createDraft(article.getId(), article.getContent(), DraftStatus.getByString(article.getStatus()));

        return APIResponse.success();
    }

    @ApiOperation("删除文章")
    @DeleteMapping("/{id}")
    public APIResponse<?> deleteArticle(@PathVariable("id") Long id) {
        articleService.deleteBy(id);
        return APIResponse.success();
    }
//
//    @ApiOperation("更新文章")
//    @PostMapping("/update")
//    public APIResponse<?> updateArticle(Article article) {
//        article.setModified(now());
//        articleService.updateArticleById(article);
//        return APIResponse.success();
//    }

    @ApiOperation("获取文章信息，分类，标签，评论等")
    @GetMapping("/meta/{articleId}")
    public APIResponse<ArticleMetaInfo> getMetaInfo(@PathVariable("articleId") Long articleId){
        return APIResponse.success(articleService.getArticleMetaInfo(articleId));
    }
}