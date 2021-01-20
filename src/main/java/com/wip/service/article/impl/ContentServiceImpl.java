/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/25 16:50
 **/
package com.wip.service.article.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.constant.ErrorConstant;
import com.wip.constant.Types;
import com.wip.constant.WebConst;
import com.wip.dao.CommentDao;
import com.wip.dao.ContentDao;
import com.wip.dao.LogDao;
import com.wip.dao.RelationShipDao;
import com.wip.exception.BusinessException;
import com.wip.model.Comment;
import com.wip.model.Content;
import com.wip.model.DraftStatus;
import com.wip.model.Log;
import com.wip.model.Meta;
import com.wip.model.RelationShip;
import com.wip.model.dto.cond.ContentCond;
import com.wip.model.vo.ContentMetaInfo;
import com.wip.service.DraftService;
import com.wip.service.article.ContentService;
import com.wip.service.cache.CacheService;
import com.wip.service.es.EsContentService;
import com.wip.service.meta.MetaService;
import com.wip.utils.IPKit;
import com.wip.utils.MapCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.wip.utils.MyStringUtil.LineNoFormat;
import static com.wip.utils.MyStringUtil.generateLineNumberForText;

@Service
@EnableAspectJAutoProxy(exposeProxy = true)
public class ContentServiceImpl implements ContentService {

    private static final MapCache cache = MapCache.single();
    @Autowired
    private ContentDao contentDao;

    @Autowired
    private MetaService metaService;

    @Autowired
    private RelationShipDao relationShipDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private EsContentService esContentService;

    @Autowired
    private DraftService draftService;

    @Autowired
    private LogDao logDao;

    @Autowired
    private CacheService cacheService;

    @Transactional
    @Override
    @CacheEvict(value = "articleCache", allEntries = true, beforeInvocation = true)
    public long addArticle(Content content) {
        if (null == content) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }

        if (StringUtils.isBlank(content.getTitle())) {
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_CAN_NOT_EMPTY);
        }

        if (content.getTitle().length() > WebConst.MAX_TITLE_COUNT) {
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_IS_TOO_LONG);
        }

        if (StringUtils.isBlank(content.getContent())) {
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_CAN_NOT_EMPTY);
        }

        if (content.getContent().length() > WebConst.MAX_CONTENT_COUNT) {
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_IS_TOO_LONG);
        }

        // 取到标签和分类
        String tags = content.getTags();
        String categories = content.getCategories();
        // 添加文章

        long id = contentDao.addArticle(content);
        draftService.createDraft(id, "", content.getContent(), DraftStatus.getByString(content.getStatus()));

        content.setContent(generateLineNumberForText(content.getContent(), LineNoFormat, true));
        esContentService.add(contentDao.getArticleById(content.getCid()));

        // 添加分类和标签
        int cid = content.getCid();
        metaService.addMetas(cid, tags, Types.TAG.getType());
        metaService.addMetas(cid, categories, Types.CATEGORY.getType());
        return id;
    }

    @Override
    @Cacheable(value = "articleCache", key = "'articleById_' + #p0")
    public Content getArticleById(Integer cid) {
        if (null == cid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        return contentDao.getArticleById(cid);
    }

    @Override
    @Transactional
    @CacheEvict(value = "articleCache", allEntries = true, beforeInvocation = true)
    public void updateArticleById(Content content) {
        // 标签和分类
        String tags = content.getTags();
        String categories = content.getCategories();

        draftService.createDraft(content.getCid(), content.getContent(), DraftStatus.getByString(content.getStatus()));
        contentDao.updateArticleById(content);
        content.setContent(generateLineNumberForText(content.getContent(), LineNoFormat, true));
        esContentService.update(content);
        int cid = content.getCid();
        relationShipDao.deleteRelationShipByCid(cid);
        metaService.addMetas(cid, tags, Types.TAG.getType());
        metaService.addMetas(cid, categories, Types.CATEGORY.getType());

    }

    @Override
    @Cacheable(value = "articleCache", key = "'articlesByCond_'+ #p0.getMd5() + '.' + #p1+'.'+#p2 ")
    public PageInfo<Content> getArticlesByCond(ContentCond contentCond, int pageNum, int pageSize) {
        if (null == contentCond) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> contentDao.getArticleByCond(contentCond));
    }

    @Override
    public PageInfo<ContentMetaInfo> getArticleMetaInfos(ContentCond contentCond, int pageNum, int pageSize) {
        ContentService contentService = (ContentService) AopContext.currentProxy();
        PageInfo<Content> articlesByCond = contentService.getArticlesByCond(contentCond, pageNum, pageSize);
        PageInfo<ContentMetaInfo> contentMetaInfoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(articlesByCond, contentMetaInfoPageInfo);
        return contentMetaInfoPageInfo;
    }

    @Override
    @Transactional
    @CacheEvict(value = "articleCache", allEntries = true, beforeInvocation = true)
    public void deleteArticleById(Integer cid) {
        if (null == cid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        // 删除文章
        // contentDao.deleteArticleById(cid);
        contentDao.updateStatusById(cid, "deleted");
        draftService.createDraftForDelete(cid, DraftStatus.PUBLISHED);
        esContentService.delete(cid.toString());

        // 同时要删除该 文章下的所有评论
        List<Comment> comments = commentDao.getCommentByCId(cid);
        if (null != comments && comments.size() > 0) {
            comments.forEach(comment -> commentDao.deleteComment(comment.getCoid()));
        }

        // 删除标签和分类关联
        List<RelationShip> relationShips = relationShipDao.getRelationShipByCid(cid);
        if (null != relationShips && relationShips.size() > 0) {
            relationShipDao.deleteRelationShipByCid(cid);
        }


    }

    @Override
    @CacheEvict(value = {"articleCache"}, allEntries = true, beforeInvocation = true)
    public void updateContentByCid(Content content) {
        if (null != content && null != content.getCid()) {
            draftService.createDraft(content.getCid(), content.getContent(), DraftStatus.getByString(content.getStatus()));
            contentDao.updateArticleById(content);
            content.setContent(generateLineNumberForText(content.getContent(), LineNoFormat, true));
            esContentService.update(content);
        }
    }

    @Override
    @Cacheable(value = "articleCache", key = "'articleByCategory_' + #p0")
    public List<Content> getArticleByCategory(String category) {
        if (null == category) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        return contentDao.getArticleByCategory(category);
    }

    @Override
    @Cacheable(value = "articleCache", key = "'articleByTags_'+ #p0")
    public List<Content> getArticleByTags(Meta tags) {
        if (null == tags) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        List<RelationShip> relationShip = relationShipDao.getRelationShipByMid(tags.getMid());
        if (null != relationShip && relationShip.size() > 0) {
            return contentDao.getArticleByTags(relationShip);
        }
        return null;
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
            contentDao.updateHitsById(cid, chits + hits);
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
