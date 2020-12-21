package com.wip.controller.v2;

import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import com.wip.constant.ErrorConstant;
import com.wip.constant.Types;
import com.wip.constant.WebConst;
import com.wip.controller.BaseController;
import com.wip.exception.BusinessException;
import com.wip.model.Comment;
import com.wip.model.Content;
import com.wip.model.Meta;
import com.wip.model.dto.ArticleInfo;
import com.wip.model.dto.MetaDto;
import com.wip.model.dto.cond.ContentCond;
import com.wip.model.vo.ContentMetaInfo;
import com.wip.service.article.ContentService;
import com.wip.service.comment.CommentService;
import com.wip.service.meta.MetaService;
import com.wip.utils.APIResponse;
import com.wip.utils.IPKit;
import com.wip.utils.TaleUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.wip.utils.APIResponse.success;

@Api("博客前台页面")
@RestController("homeControllerV2")
@RequestMapping("/v2")
public class HomeController extends BaseController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MetaService metaService;


    @GetMapping(value = "/")
    public String index(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "页数", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    int page,
            @ApiParam(name = "limit", value = "每页数量", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "5")
                    int limit
    ) {
        ContentCond cond = new ContentCond();
        cond.setHowToOrder("orderWeight DESC, modified DESC");
        cond.setStatus(ContentCond.PUBLISH);
        PageInfo<Content> articles = contentService.getArticlesByCond(cond, page, limit);

        request.setAttribute("articles", articles);
        return "blog/home";
    }

    @ApiOperation("归档内容")
    @GetMapping(value = "/archives")
    public APIResponse<PageInfo<ContentMetaInfo>> archives(
            @ApiParam(name = "page", value = "页数", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    int page,
            @ApiParam(name = "pageSize", value = "每页数量", required = false)
            @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                    int pageSize
    ) {
        ContentCond cond = new ContentCond();
        cond.setStatus(ContentCond.PUBLISH);
        cond.setHowToOrder("created DESC");
        PageInfo<Content> articles = contentService.getArticlesByCond(cond, page, pageSize);
        List<ContentMetaInfo> collect = articles.getList().stream().map(ContentMetaInfo::convertFrom).collect(Collectors.toList());
        PageInfo<ContentMetaInfo> articlesMetaInfo = PageInfo.of(collect);
        return success(articlesMetaInfo);
    }

    @ApiOperation("获取所有分类")
    @GetMapping(value = "/categories/all")
    public APIResponse<List<MetaDto>> categories() {
        List<MetaDto> categories = metaService.getMetaList(Types.CATEGORY.getType(), null, WebConst.MAX_POSTS);
        return success(categories);
    }

    @ApiOperation("分类下所有文章")
    @GetMapping(value = "/categories/{name}")
    public APIResponse<List<Content>> categoriesDetail(
            @ApiParam(name = "name", value = "分类名称", required = true)
            @PathVariable("name") String name
    ) {
        Meta category = metaService.getMetaByName(Types.CATEGORY.getType(), name);
        if (null == category.getName()) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        List<Content> articles = contentService.getArticleByCategory(category.getName());
        return success(articles);
    }

    @ApiOperation("获取所有标签")
    @GetMapping(value = "/tags")
    public APIResponse<List<MetaDto>> tags(HttpServletRequest request) {
        // 获取标签
        List<MetaDto> tags = metaService.getMetaList(Types.TAG.getType(), null, WebConst.MAX_POSTS);
        return success(tags);
    }

    @ApiOperation("标签下所有文章")
    @GetMapping(value = "/tags/{name}")
    public APIResponse<List<Content>> tagsDetail(
            HttpServletRequest request,
            @ApiParam(name = "name", value = "标签名", required = true)
            @PathVariable("name")
                    String name
    ) {
        Meta tags = metaService.getMetaByName(Types.TAG.getType(), name);
        List<Content> articles = contentService.getArticleByTags(tags);
        request.setAttribute("articles", articles);
        return success(articles);
    }

    @ApiOperation("文章内容页")
    @GetMapping(value = "/detail/{cid}")
    @CrossOrigin(methods = RequestMethod.GET)
    public APIResponse<ArticleInfo> detail(
            @ApiParam(name = "cid", value = "文章主键", required = true)
            @PathVariable("cid")
                    Integer cid,
            HttpServletRequest request
    ) {
        Content article = contentService.getArticleById(cid);
        article.setToc(TaleUtils.getHeadLineFrom(article.getContent()));
        String md = TaleUtils.mdToHtml(article.getContent());
        article.setContent(md);

        if (!isFromSameIp(cid, request)) {
            // 更新文章的点击量
            this.updateArticleHits(article.getCid(), article.getHits());
        }
        // 获取评论
        List<Comment> comments = commentService.getCommentsByCId(cid);
        return success(new ArticleInfo(article,comments));
    }

    private boolean isFromSameIp(Integer cid, HttpServletRequest request) {
        String uniqHitKey = String.format("%s::%s", IPKit.getIpAddressByRequest(request), cid);
        String uniqHitValue = cache.get(uniqHitKey);
        if (Objects.nonNull(uniqHitValue)) {
            return true;
        } else {
            cache.set(uniqHitKey, "y", TimeUnit.DAYS.toMillis(1));
            return false;
        }
    }

    /**
     * 更新文章的点击率
     *
     * @param cid
     * @param chits
     */
    private void updateArticleHits(Integer cid, Integer chits) {
        Integer hits = cache.hget("article", "hits");
        if (chits == null) {
            chits = 0;
        }
        hits = null == hits ? 1 : hits + 1;
        if (hits >= WebConst.HIT_BUFFER_SIZE) {
            Content temp = new Content();
            temp.setCid(cid);
            temp.setHits(chits + hits);
            contentService.updateContentByCid(temp);
            cache.hset("article", "hits", null);
        } else {
            cache.hset("article", "hits", hits);
        }

    }

    @PostMapping(value = "/comment")
    @ResponseBody
    public APIResponse comment(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(name = "cid", required = true) Integer cid,
                               @RequestParam(name = "coid", required = false) Integer coid,
                               @RequestParam(name = "author", required = false) String author,
                               @RequestParam(name = "email", required = false) String email,
                               @RequestParam(name = "url", required = false) String url,
                               @RequestParam(name = "content", required = true) String content,
                               @RequestParam(name = "csrf_token", required = true) String csrf_token
    ) {

        String ref = request.getHeader("Referer");
        if (StringUtils.isBlank(ref) || StringUtils.isBlank(csrf_token)) {
            return APIResponse.fail("访问失败");
        }

        String token = cache.hget(Types.CSRF_TOKEN.getType(), csrf_token);
        if (StringUtils.isBlank(token)) {
            return APIResponse.fail("访问失败");
        }

        if (null == cid || StringUtils.isBlank(content)) {
            return APIResponse.fail("请输入完整后评论");
        }

        if (StringUtils.isNotBlank(author) && author.length() > 50) {
            return APIResponse.fail("姓名过长");
        }

        if (StringUtils.isNotBlank(email) && !TaleUtils.isEmail(email)) {
            return APIResponse.fail("请输入正确的邮箱格式");
        }

        if (StringUtils.isNotBlank(url) && !TaleUtils.isURL(url)) {
            return APIResponse.fail("请输入正确的网址格式");
        }

        if (content.length() > 200) {
            return APIResponse.fail("请输入200个字符以内的评价");
        }

        String val = IPKit.getIpAddressByRequest(request) + ":" + cid;
        Integer count = cache.hget(Types.COMMENTS_FREQUENCY.getType(), val);
        if (null != count && count > 0) {
            return APIResponse.fail("您发表的评论太快了，请过会再试");
        }

        author = TaleUtils.cleanXSS(author);
        content = TaleUtils.cleanXSS(content);

        author = EmojiParser.parseToAliases(author);
        content = EmojiParser.parseToAliases(content);


        Comment comments = new Comment();
        comments.setAuthor(author);
        comments.setCid(cid);
        comments.setIp(request.getRemoteAddr());
        comments.setUrl(url);
        comments.setContent(content);
        comments.setEmail(email);
        comments.setParent(coid);

        try {
            commentService.addComment(comments);
            cookie("tale_remember_author", URLEncoder.encode(author, "UTF-8"), 7 * 24 * 60 * 60, response);
            cookie("tale_remember_mail", URLEncoder.encode(email, "UTF-8"), 7 * 24 * 60 * 60, response);
            if (StringUtils.isNotBlank(url)) {
                cookie("tale_remember_url", URLEncoder.encode(url, "UTF-8"), 7 * 24 * 60 * 60, response);
            }
            // 设置对每个文章1分钟可以评论一次
            cache.hset(Types.COMMENTS_FREQUENCY.getType(), val, 1, 60);

            return success();

        } catch (Exception e) {
            e.printStackTrace();
            // throw BusinessException.withErrorCode(ErrorConstant.Comment.ADD_NEW_COMMENT_FAIL);
            return APIResponse.fail(e.getMessage());
        }

    }

    private void cookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }


}
