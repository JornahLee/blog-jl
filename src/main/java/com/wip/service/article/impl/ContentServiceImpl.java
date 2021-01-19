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
import com.wip.dao.DraftDao;
import com.wip.dao.RelationShipDao;
import com.wip.exception.BusinessException;
import com.wip.model.Comment;
import com.wip.model.Content;
import com.wip.model.Draft;
import com.wip.model.Meta;
import com.wip.model.RelationShip;
import com.wip.model.dto.cond.ContentCond;
import com.wip.model.vo.ContentMetaInfo;
import com.wip.service.DraftService;
import com.wip.service.article.ContentService;
import com.wip.service.es.EsContentService;
import com.wip.service.meta.MetaService;
import com.wip.utils.TextDifferenceChecker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.wip.utils.MyStringUtil.LineNoFormat;
import static com.wip.utils.MyStringUtil.generateLineNumberForText;

@Service
@EnableAspectJAutoProxy(exposeProxy = true)
public class ContentServiceImpl implements ContentService {

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
        //todo 添加草稿
        draftService.createDraft(id,"",content.getContent());

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
        contentDao.deleteArticleById(cid);
        esContentService.delete(cid.toString());

        // 同时要删除该 文章下的所有评论
        List<Comment> comments = commentDao.getCommentByCId(cid);
        if (null != comments && comments.size() > 0) {
            comments.forEach(comment -> {
                commentDao.deleteComment(comment.getCoid());
            });
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
}
