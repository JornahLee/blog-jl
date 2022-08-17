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
import com.google.common.collect.Lists;
import com.jornah.cache.CacheService;
import com.jornah.constant.ArticleStatus;
import com.jornah.constant.ArticleType;
import com.jornah.constant.ExceptionType;
import com.jornah.constant.WebConst;
import com.jornah.dao.ArticleDao;
import com.jornah.dao.CategoryDao;
import com.jornah.dao.LogDao;
import com.jornah.dao.TagDao;
import com.jornah.exception.BusinessException;
import com.jornah.model.DraftStatus;
import com.jornah.model.UserInfo;
import com.jornah.model.converter.ArticleConverter;
import com.jornah.model.dto.ArticleSaveBo;
import com.jornah.model.entity.Article;
import com.jornah.model.entity.Log;
import com.jornah.model.qo.ArticleQo;
import com.jornah.model.vo.ArticleMetaInfo;
import com.jornah.model.vo.ArticleVo;
import com.jornah.service.DraftService;
import com.jornah.service.article.ArticleService;
import com.jornah.service.es.EsContentService;
import com.jornah.utils.EncryptUtil;
import com.jornah.utils.IPKit;
import com.jornah.utils.MapCache;
import com.jornah.utils.PageUtil;
import com.jornah.utils.WebRequestHelper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.jornah.constant.ExceptionType.BAD_VERSION;
import static com.jornah.model.enums.ArticleStatus.DELETED;
import static com.jornah.model.enums.ArticleStatus.DRAFT;
import static com.jornah.model.enums.ArticleStatus.PUBLISHED;

@Service
@EnableAspectJAutoProxy(exposeProxy = true)
public class ArticleServiceImpl implements ArticleService {

    private static final MapCache cache = MapCache.single();
    public static final int DEFAULT_ARTICLE_ID = 44;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private CacheService cacheService;
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
        encryptDiary(articleSaveBo);
        Article article = ArticleConverter.INSTANCE.toEntity(articleSaveBo);
        if (Objects.isNull(article.getId())) {
            article.setCreated(Instant.now());
            article.setUpdated(Instant.now());
            articleDao.insert(article);
            esContentService.add(article);
        } else {
            checkContentVersion(articleSaveBo);
            article.setUpdated(Instant.now());
            articleDao.updateById(article);
            esContentService.update(article);
        }

        return article.getId();
    }

    @SneakyThrows
    private void encryptDiary(ArticleSaveBo articleSaveBo) {
        if (!ArticleType.DIARY.equals(articleSaveBo.getType())) {
            return;
        }
        Assert.notNull(articleSaveBo.getPassphrase(), "需提供passphrase");
        String encryptText = EncryptUtil.encrypt(articleSaveBo.getContent(), articleSaveBo.getPassphrase());
        articleSaveBo.setContent(encryptText);

    }

    private void checkContentVersion(ArticleSaveBo articleSaveBo) {
        // 兼容前端未开发完成不检查版本
        if (Objects.isNull(articleSaveBo.getVersion())) {
            return;
        }
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<Article>().eq(Article::getId, articleSaveBo.getId());
        Article fromDb = articleDao.selectOne(queryWrapper);
        if (articleSaveBo.getVersion() - fromDb.getVersion() != 1) {
            throw BusinessException.of(BAD_VERSION, "保存失败，不是基于最新版本修改");
        }
    }

    @Override
    public ArticleVo getArticleBy(Long arId, String passphrase) {
        Article article = articleDao.selectById(arId);
        decryptDiaryContent(passphrase, article);
        return ArticleConverter.INSTANCE.toVo(article);
    }

    private void decryptDiaryContent(String passphrase, Article article) {
        if (!ArticleType.DIARY.equalTo(article.getType())) {
            return ;
        }
        try {
            String plainText = EncryptUtil.decrypt(article.getContent(), passphrase);
            article.setContent(plainText);
        } catch (Exception e) {
            throw BusinessException.of(ExceptionType.BAD_PASSPHRASE,"bad passphrase，解析失败");
        }

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
    public PageInfo<ArticleVo> getArticlesOrderBy(@Validated ArticleQo articleQo) {
        PageInfo<Article> pageInfo = PageHelper.startPage(articleQo.getPageNum(), articleQo.getPageSize())
                .doSelectPageInfo(() -> articleDao.findArticlesByStatus(getAdminAccessStatus()));
        return PageUtil.toVo(pageInfo, ArticleConverter.INSTANCE::toVo);
    }

    private List<String> getAdminAccessStatus() {
        UserInfo currentUserInfo = WebRequestHelper.getCurrentUserInfo();
        return !currentUserInfo.isTourist() ?
                Lists.newArrayList(PUBLISHED.getValue(), DELETED.getValue(), DRAFT.getValue()) :
                Lists.newArrayList(PUBLISHED.getValue());
    }

    @Transactional
    @Override
    public void deleteBy(Long arId) {
        // 删除文章

        articleDao.updateStatusById(arId, ArticleStatus.DELETED.toString());
//        draftService.createDraftForDelete(arId, DraftStatus.PUBLISHED);
//        esContentService.delete(arId.toString());

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
                .doSelectPageInfo(() -> articleDao.findArticlesByCategory(cateId, getAdminAccessStatus()));
        return PageUtil.toVo(pageInfo, ArticleConverter.INSTANCE::toVo);
    }

    @Override
    public PageInfo<ArticleVo> getArticleByTag(Long tagId, int pageNum, int pageSize) {
        PageInfo<Article> pageInfo = PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> articleDao.findArticlesByTag(tagId, getAdminAccessStatus()));
        return PageUtil.toVo(pageInfo, ArticleConverter.INSTANCE::toVo);
    }

    @Override
    public List<ArticleVo> getRecommendArticle(int size) {
        List<Article> articles = articleDao.findByRecommend(size);
        return articles.stream()
                .map(ArticleConverter.INSTANCE::toVo)
                .peek(article -> {
                    String sub = article.getContent().substring(0, 100);
                    article.setContent(sub);
                })
                .collect(Collectors.toList());
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

    @Override
    public ArticleMetaInfo getArticleMetaInfo(Long articleId) {
//        cacheService.getValue()
        return ArticleMetaInfo.builder().tags(tagDao.findTagBy(articleId))
                .category(categoryDao.findCategoryBy(articleId)).build();
    }

    @Override
    public int articleCount() {
        QueryWrapper<Article> query = new QueryWrapper<>();
        return articleDao.selectCount(query);
    }

    @Override
    public Article firstArticle() {
        LambdaQueryWrapper<Article> query = new LambdaQueryWrapper<>();
        query.orderByDesc(Article::getCreated);
        return articleDao.selectOne(query);
    }

    @Override
    public long getNextOrLastArticle(Long articleId, boolean next, String byType) {
        List<Long> allId = articleDao.findAllIdBy();
        for (int index = 1; index < allId.size() - 1; index++) {
            if (Objects.equals(allId.get(index), articleId)) {
                return next ? allId.get(index + 1) : allId.get(index - 1);
            }
        }
        return DEFAULT_ARTICLE_ID;
    }
}
