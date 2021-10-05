//package com.wip.controller.v1.admin;
//
//import com.github.pagehelper.PageInfo;
//import com.wip.constant.LogActions;
//import com.wip.constant.Types;
//import com.wip.controller.BaseController;
//import com.wip.model.dto.cond.ContentCond;
//import com.wip.model.dto.cond.MetaCond;
//import com.wip.model.newP.Article;
//import com.wip.model.vo.ArticleVo;
//import com.wip.service.article.ArticleService;
//import com.wip.service.log.LogService;
//import com.wip.utils.APIResponse;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//import java.util.Map;
//
//import static java.time.Instant.now;
//
//@Api("文章管理")
//@Controller
//@RequestMapping("/admin/article")
//public class ArticleController extends BaseController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);
//
//    @Autowired
//    private MetaService metaService;
//
//    @Autowired
//    private ArticleService articleService;
//
//    @Autowired
//    private LogService logService;
//
//    @ApiOperation("文章页")
//    @GetMapping(value = "")
//    public String index(
//            HttpServletRequest request,
//            @RequestParam Map<String,String> filters,
//            @ApiParam(name = "page", value = "页数", required = false)
//            @RequestParam(name = "page", required = false, defaultValue = "1")
//            int page,
//            @ApiParam(name = "limit", value = "每页数量", required = false)
//            @RequestParam(name = "limit", required = false, defaultValue = "15")
//            int limit
//    ) {
//        ContentCond contentCond = new ContentCond();
//        contentCond.setHowToOrder("modified DESC");
//        // ORDER BY  c.orderWeight DESC ,  c.modified DESC , created DESC
//        contentCond.setValuesFrom(filters);
//        PageInfo<ArticleVo> articles = articleService.getArticleMetaInfos(contentCond, page, limit);
//        request.setAttribute("articles",articles);
//        List<Meta> categories = metaService.getMetas(MetaCond.of(Types.CATEGORY.getType()));
//        request.setAttribute("categories",categories);
//        return "admin/article_list";
//    }
//
//    @ApiOperation("发布新文章页")
//    @GetMapping(value = "/publish")
//    public String newArticle(HttpServletRequest request) {
//        List<Meta> metas = metaService.getMetas(MetaCond.of(Types.CATEGORY.getType()));
//        request.setAttribute("categories",metas);
//        return "admin/article_edit";
//    }
//
//    @ApiOperation("文章编辑页")
//    @GetMapping(value = "/{cid}")
//    public String editArticle(
//            @ApiParam(name = "cid", value = "文章编号", required = true)
//            @PathVariable
//            Integer cid,
//            HttpServletRequest request
//    ) {
//        Article article = articleService.getArticleById(cid);
//        request.setAttribute("contents", article);
//        List<Meta> categories = metaService.getMetas(MetaCond.of(Types.CATEGORY.getType()));
//        request.setAttribute("categories", categories);
//        request.setAttribute("active", "article");
//        return "admin/article_edit";
//    }
//
//    @ApiOperation("编辑保存文章")
//    @PostMapping("/modify")
//    @ResponseBody
//    public APIResponse modifyArticle(
//            HttpServletRequest request,
//            @ApiParam(name = "cid", value = "文章主键", required = true)
//            @RequestParam(name = "cid", required = true)
//            Integer cid,
//            @ApiParam(name = "title", value = "标题", required = true)
//            @RequestParam(name = "title", required = true)
//            String title,
//            @ApiParam(name = "titlePic", value = "标题图片", required = false)
//            @RequestParam(name = "titlePic", required = false)
//            String titlePic,
//            @ApiParam(name = "slug", value = "内容缩略名", required = false)
//            @RequestParam(name = "slug", required = false)
//            String slug,
//            @ApiParam(name = "content", value = "内容", required = true)
//            @RequestParam(name = "content", required = true)
//            String content,
//            @ApiParam(name = "type", value = "文章类型", required = true)
//            @RequestParam(name = "type", required = true)
//            String type,
//            @ApiParam(name = "status", value = "文章状态", required = true)
//            @RequestParam(name = "status", required = true)
//            String status,
//            @ApiParam(name = "tags", value = "标签", required = false)
//            @RequestParam(name = "tags", required = false)
//            String tags,
//            @ApiParam(name = "categories", value = "分类", required = false)
//            @RequestParam(name = "categories", required = false, defaultValue = "默认分类")
//            String categories,
//            @ApiParam(name = "allowComment", value = "是否允许评论", required = true)
//            @RequestParam(name = "allowComment", required = true)
//            Boolean allowComment,
//            @ApiParam(name = "orderWeight", value = "排序加权", required = true)
//            @RequestParam(name = "orderWeight", required = true)
//            Integer orderWeight
//    ) {
//        Article articleDomain = new Article();
//        articleDomain.setTitle(title);
//        articleDomain.setCid(cid);
//        articleDomain.setTitlePic(titlePic);
//        articleDomain.setSlug(slug);
//        articleDomain.setContent(content);
//        articleDomain.setType(type);
//        articleDomain.setStatus(status);
//        articleDomain.setTags(tags);
//        articleDomain.setCategories(categories);
//        articleDomain.setAllowComment(allowComment ? 1: 0);
//        articleDomain.setOrder(orderWeight);
//        articleDomain.setModified(now());
//
//        articleService.updateArticleById(articleDomain);
//
//        return APIResponse.success();
//    }
//
//
//    @ApiOperation("发布新文章")
//    @PostMapping(value = "/publish")
//    @ResponseBody
//    public APIResponse publishArticle(
//            @ApiParam(name = "title", value = "标题", required = true)
//            @RequestParam(name = "title", required = true)
//            String title,
//            @ApiParam(name = "titlePic", value = "标题图片", required = false)
//            @RequestParam(name = "titlePic", required = false)
//            String titlePic,
//            @ApiParam(name = "slug", value = "内容缩略名", required = false)
//            @RequestParam(name = "slug", required = false)
//            String slug,
//            @ApiParam(name = "content", value = "内容", required = true)
//            @RequestParam(name = "content", required = true)
//            String content,
//            @ApiParam(name = "type", value = "文章类型", required = true)
//            @RequestParam(name = "type", required = true)
//            String type,
//            @ApiParam(name = "status", value = "文章状态", required = true)
//            @RequestParam(name = "status", required = true)
//            String status,
//            @ApiParam(name = "categories", value = "文章分类", required = false)
//            @RequestParam(name = "categories", required = false, defaultValue = "默认分类")
//            String categories,
//            @ApiParam(name = "tags", value = "文章标签", required = false)
//            @RequestParam(name = "tags", required = false)
//            String tags,
//            @ApiParam(name = "allowComment", value = "是否允许评论", required = true)
//            @RequestParam(name = "allowComment", required = true)
//            Boolean allowComment,
//            @ApiParam(name = "orderWeight", value = "排序加权", required = true)
//            @RequestParam(name = "orderWeight", required = true)
//            Integer orderWeight
//    ) {
//        Article articleDomain = new Article();
//        articleDomain.setTitle(title);
//        articleDomain.setTitlePic(titlePic);
//        articleDomain.setSlug(slug);
//        articleDomain.setContent(content);
//        articleDomain.setType(type);
//        articleDomain.setStatus(status);
//        articleDomain.setHits(1);
//        articleDomain.setCommentsNum(0);
//        // 只允许博客文章有分类，防止作品被收入分类
//        articleDomain.setTags(type.equals(Types.ARTICLE.getType()) ? tags : null);
//        articleDomain.setCategories(type.equals(Types.ARTICLE.getType()) ? categories : null);
//        articleDomain.setAllowComment(allowComment ? 1 : 0);
//        articleDomain.setOrder(orderWeight);
//        LOGGER.debug("param-orderWeight:{}",orderWeight);
//        LOGGER.debug("contentDomain-orderWeight:{}", articleDomain.getOrder());
//
//        // 添加文章
//        articleService.addArticle(articleDomain);
//
//        return APIResponse.success();
//    }
//
//    @ApiOperation("删除文章")
//    @PostMapping("/delete")
//    @ResponseBody
//    public APIResponse deleteArticle(
//            @ApiParam(name = "cid", value = "文章ID", required = true)
//            @RequestParam(name = "cid", required = true)
//            Integer cid,
//            HttpServletRequest request
//    ) {
//        // 删除文章
//        articleService.deleteArticleById(cid);
//        // 写入日志
//        logService.addLog(LogActions.DEL_ARTICLE.getAction(), cid+"",request.getRemoteAddr(),this.getUid(request));
//        return APIResponse.success();
//    }
//
//
//}