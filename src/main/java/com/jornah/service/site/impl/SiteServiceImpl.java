///**
// * Created by IntelliJ IDEA.
// * User: Jornah Lee
// * DateTime: 2018/8/2 8:48
// **/
//package com.wip.service.site.impl;
//
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import com.wip.constant.Types;
//import com.wip.dao.AttachDao;
//import com.wip.dao.CommentDao;
//import com.wip.dao.ArticleDao;
//import com.wip.model.Comment;
//import com.wip.model.newP.Article;
//import com.wip.model.dto.StatisticsDto;
//import com.wip.model.dto.cond.CommentCond;
//import com.wip.model.dto.cond.ContentCond;
//import com.wip.service.site.SiteService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class SiteServiceImpl implements SiteService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);
//
//    @Autowired
//    private CommentDao commentDao;
//
//    @Autowired
//    private ArticleDao articleDao;
//
//    @Autowired
//    private AttachDao attAchDao;
//
//
//
//    @Override
//
//    public List<Comment> getComments(int limit) {
//        LOGGER.debug("Enter recentComments method: limit={}", limit);
//        if (limit < 0 || limit > 10) {
//            limit = 10;
//        }
//        LOGGER.debug("Exit recentComments method");
//        PageInfo<Comment> commentPageInfo = PageHelper.startPage(1, limit).doSelectPageInfo(() -> commentDao.getCommentsByCond(new CommentCond()));
//        return commentPageInfo.getList();
//    }
//
//    @Override
//
//    public List<Article> getNewArticles(int limit) {
//        LOGGER.debug("Enter recentArticles method:limit={}",limit);
//        if (limit < 0 || limit > 10) {
//            limit = 10;
//        }
//        PageInfo<Article> contentPageInfo = PageHelper.startPage(1, limit)
//                .doSelectPageInfo(() -> articleDao.getArticleByCond(new ContentCond()));
//        LOGGER.debug("Exit recentArticles method");
//        return contentPageInfo.getList();
//    }
//
//    @Override
//
//    public StatisticsDto getStatistics() {
//        LOGGER.debug("Enter recentStatistics method");
//
//        // 文章总数
//        Long articles = articleDao.getArticleCount();
//
//        // 评论总数
//        Long comments = commentDao.getCommentCount();
//
//        // 链接数
//        Long links = metaDao.getMetasCountByType(Types.LINK.getType());
//
//        // 获取附件数
//        Long attAches = attAchDao.getAttachCount();
//
//        StatisticsDto rs = new StatisticsDto();
//        rs.setArticles(articles);
//        rs.setComments(comments);
//        rs.setLinks(links);
//        rs.setAttachs(attAches);
//        LOGGER.debug("Exit recentStatistics method");
//        return rs;
//    }
//}
