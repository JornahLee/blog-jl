package com.jornah.controller.v2;


import com.github.pagehelper.PageInfo;
import com.jornah.dao.DraftDao;
import com.jornah.model.dto.ArticleSaveBo;
import com.jornah.model.newP.Article;
import com.jornah.model.DraftStatus;
import com.jornah.model.qo.ArticleQo;
import com.jornah.model.vo.ArticleVo;
import com.jornah.service.DraftService;
import com.jornah.service.article.ArticleService;
import com.jornah.utils.APIResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static java.time.Instant.now;

@RestController()
@RequestMapping("/blog/article")
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private DraftService draftService;
    @Autowired
    private DraftDao draftDao;

    @ApiOperation("发布新文章")
    @PostMapping(value = "/publish")
    public APIResponse<?> publishArticle(ArticleSaveBo articleSaveBo) {
        articleSaveBo.getArticle().setUpdated(now());
        articleSaveBo.getArticle().setCreated(now());
        articleService.addArticle(articleSaveBo);
        return APIResponse.success();
    }
    @ApiOperation("查询文档")
    @GetMapping(value = "/{id}")
    public APIResponse<ArticleVo> getArticle(@PathVariable Long id ) {
        ArticleVo articleVo = articleService.getArticleBy(id);
        return APIResponse.success( articleVo);
    }

    @ApiOperation("查询文档")
    @PostMapping(value = "/list")
    public APIResponse<?> getArticleList(@RequestBody ArticleQo qo) {
        PageInfo orderBy = articleService.getArticlesOrderBy(qo);
        return APIResponse.success(orderBy);
    }

    @ApiOperation("保存草稿")
    @PostMapping(value = "/draft/save")
    public APIResponse<?> saveDraft(Article article) {
        draftService.createDraft(article.getId(), article.getContent(), DraftStatus.getByString(article.getStatus()));

        return APIResponse.success();
    }

//    @ApiOperation("删除文章")
//    @PostMapping("/delete")
//    public APIResponse<?> deleteArticle(@ApiParam(name = "cid", value = "文章ID", required = true)
//                                        @RequestParam(name = "cid") Integer cid) {
//        // 删除文章
//        articleService.deleteArticleById(cid);
//        return APIResponse.success();
//    }
//
//    @ApiOperation("更新文章")
//    @PostMapping("/update")
//    public APIResponse<?> updateArticle(Article article) {
//        article.setModified(now());
//        articleService.updateArticleById(article);
//        return APIResponse.success();
//    }
}
