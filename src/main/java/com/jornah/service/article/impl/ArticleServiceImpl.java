/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/25 16:50
 **/
package com.jornah.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jornah.constant.WebConst;
import com.jornah.dao.CategoryDao;
import com.jornah.dao.CommentDao;
import com.jornah.dao.ArticleDao;
import com.jornah.dao.LogDao;
import com.jornah.dao.TagDao;
import com.jornah.model.converter.ArticleConverter;
import com.jornah.model.dto.ArticleSaveBo;
import com.jornah.model.newP.Article;
import com.jornah.model.DraftStatus;
import com.jornah.model.Log;
import com.jornah.model.qo.ArticleQo;
import com.jornah.model.vo.ArticleVo;
import com.jornah.service.DraftService;
import com.jornah.service.article.ArticleService;
import com.jornah.service.cache.CacheService;
import com.jornah.service.es.EsContentService;
import com.jornah.utils.IPKit;
import com.jornah.utils.MapCache;
import com.jornah.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@EnableAspectJAutoProxy(exposeProxy = true)
public class ArticleServiceImpl implements ArticleService {

    private static final MapCache cache = MapCache.single();
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private EsContentService esContentService;

    @Autowired
    private DraftService draftService;

    @Autowired
    private LogDao logDao;

    @Transactional
    @Override
    public long saveOrUpdate(ArticleSaveBo articleSaveBo) {
        Article article = ArticleConverter.INSTANCE.toEntity(articleSaveBo);
        if (Objects.isNull(article.getId())) {
            article.setCreated(Instant.now());
            article.setUpdated(Instant.now());
            articleDao.insert(article);
            //todo 元数据放到另外一个接口中
//            articleSaveBo.getTagIds().forEach(tId -> tagDao.insertMap(article.getId(), tId));
//            articleSaveBo.getCategoryIds().forEach(caId -> categoryDao.insertMap(article.getId(), caId));
        } else {
            article.setUpdated(Instant.now());
            articleDao.updateById(article);
        }
//        draftService.createDraft(article.getId(), "", article.getContent(), DraftStatus.getByString(article.getStatus()));
        //todo 搜索 根据行号跳转功能 待重构
//        articleSaveBo.setContent(generateLineNumberForText(articleSaveBo.getContent(), LineNoFormat, true));
//        esContentService.add(article);
        return article.getId();
    }

    @Override
    public ArticleVo getArticleBy(Long arId) {
        Article article = articleDao.selectById(arId);
        return ArticleConverter.INSTANCE.toVo(article);
    }

    public List<ArticleVo> getArticlesBy(List<Long> arIds) {
        return arIds.stream().map(this::getArticleBy).collect(Collectors.toList());
    }

    @Transactional
    public void updateArticleById(Article article) {
        // 标签和分类
//        String tags = article.getTags();
//        String categories = article.getCategories();
//
//        draftService.createDraft(article.getCid(), article.getContent(), DraftStatus.getByString(article.getStatus()));
//        articleDao.updateArticleById(article);
//        article.setContent(generateLineNumberForText(article.getContent(), LineNoFormat, true));
//        esContentService.update(article);
//        int cid = article.getCid();
//        relationShipDao.deleteRelationShipByCid(cid);
//        metaService.addMetas(cid, tags, Types.TAG.getType());
//        metaService.addMetas(cid, categories, Types.CATEGORY.getType());

    }

    @Override
    public PageInfo<ArticleVo> getArticlesOrderBy(ArticleQo articleQo) {
        PageInfo<Article> pageInfo = PageHelper.startPage(articleQo.getPageNum(), articleQo.getPageSize())
                .doSelectPageInfo(() -> articleDao.findArticles());
        return PageUtil.toVo(pageInfo, ArticleConverter.INSTANCE::toVo);
    }

    @Transactional
    public void deleteArticleById(Long arId) {
        // 删除文章
        // contentDao.deleteArticleById(arId);
        articleDao.updateStatusById(arId, "deleted");
        draftService.createDraftForDelete(arId, DraftStatus.PUBLISHED);
        esContentService.delete(arId.toString());

        // 同时要删除该 文章下的所有评论
//        List<Comment> comments = commentDao.getCommentByCId(arId);
//        if (null != comments && comments.size() > 0) {
//            comments.forEach(comment -> commentDao.deleteComment(comment.getCoid()));
//        }
        tagDao.deleteMapBy(arId);
        categoryDao.deleteMapBy(arId);
    }

    public void updateContentBy(Long arId, String content, String status) {
        draftService.createDraft(arId, content, DraftStatus.getByString(status));
        Article article = Article.builder().id(arId).content(content).build();
        articleDao.updateById(article);
//        article.setContent(generateLineNumberForText(article.getContent(), LineNoFormat, true));
        esContentService.update(article);
    }

    @Override
    public PageInfo<ArticleVo> getArticleByCate(Long cateId, int pageNum, int pageSize) {
        PageInfo<Article> pageInfo = PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> articleDao.findArticlesByCategory(cateId));
        return PageUtil.toVo(pageInfo, ArticleConverter.INSTANCE::toVo);
    }

    @Override
    public PageInfo<ArticleVo> getArticleByTag(Long tagId, int pageNum, int pageSize) {
        PageInfo<Article> pageInfo = PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> articleDao.findArticlesByTag(tagId));
        return PageUtil.toVo(pageInfo, ArticleConverter.INSTANCE::toVo);
    }


    /**
     * 更新文章的点击率
     *
     * @param cid
     * @param chits
     */
    @Override
    public void updateArticleHits(Integer cid, Integer chits) {
        Integer hits = cache.hget("article", "hits");
        if (chits == null) {
            chits = 0;
        }
        hits = null == hits ? 1 : hits + 1;
        if (hits >= WebConst.HIT_BUFFER_SIZE) {
            articleDao.updateHitsById(cid, chits + hits);
            cache.hset("article", "hits", null);
        } else {
            cache.hset("article", "hits", hits);
        }
    }

    @Override
    public boolean isFromSameIp(Integer cid, HttpServletRequest request) {
        String uniqHitKey = String.format("%s::%s", IPKit.getIpAddressByRequest(request), cid);
        String uniqHitValue = cache.get(uniqHitKey);
        if (Objects.nonNull(uniqHitValue)) {
            return true;
        } else {
            cache.set(uniqHitKey, "y", TimeUnit.DAYS.toMillis(1));
            return false;
        }
    }

    @Override
    @Async
    public void logVisit(Integer cid, String detail, HttpServletRequest request) {
        Log log = new Log();
        log.setAction("浏览文章");
        log.setData(detail);
        log.setIp(IPKit.getIpAddressByRequest(request));
        logDao.addLog(log);
    }
}
